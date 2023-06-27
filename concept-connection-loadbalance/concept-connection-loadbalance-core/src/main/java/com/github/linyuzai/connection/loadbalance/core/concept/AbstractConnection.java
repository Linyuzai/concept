package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.HeartbeatSendErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncodeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 连接抽象类
 */
@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    protected final Map<Object, Object> metadata = Collections.synchronizedMap(new LinkedHashMap<>());

    protected final List<MessageSendInterceptor> messageSendInterceptors = new CopyOnWriteArrayList<>();

    @NonNull
    protected String type;

    @NonNull
    protected MessageRetryStrategy messageRetryStrategy;

    @NonNull
    protected MessageEncoder messageEncoder;

    @NonNull
    protected MessageDecoder messageDecoder;

    @NonNull
    protected ConnectionLoadBalanceConcept concept;

    /**
     * 是否还存活
     */
    protected volatile boolean alive;

    /**
     * 最后一次心跳时间
     */
    protected long lastHeartbeat;

    public AbstractConnection(@NonNull String type) {
        this(type, null);
    }

    public AbstractConnection(@NonNull String type, Map<Object, Object> metadata) {
        this.type = type;
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
        this.alive = true;
        this.lastHeartbeat = System.currentTimeMillis();
    }

    @Override
    public void send(@NonNull Message message) {
        if (message instanceof PingMessage) {
            Runnable ping = () -> ping((PingMessage) message);
            try {
                ping.run();
            } catch (Throwable e) {
                messageRetryStrategy.retry(ping,
                        new HeartbeatSendErrorEvent(this, message, e));
            }
        } else if (message instanceof PongMessage) {
            Runnable pong = () -> pong((PongMessage) message);
            try {
                pong.run();
            } catch (Throwable e) {
                messageRetryStrategy.retry(pong,
                        new HeartbeatSendErrorEvent(this, message, e));
            }
        } else {
            for (MessageSendInterceptor interceptor : messageSendInterceptors) {
                if (!interceptor.intercept(message, this)) {
                    return;
                }
            }
            Object encode;
            try {
                MessageEncoder encoder = getMessageEncoder();
                encode = encoder.encode(message);
            } catch (Throwable e) {
                concept.getEventPublisher().publish(new MessageEncodeErrorEvent(this, e));
                return;
            }

            Runnable send = () -> doSend(encode);
            try {
                send.run();
            } catch (Throwable e) {
                messageRetryStrategy.retry(send,
                        new MessageSendErrorEvent(this, message, e));
            }
        }
    }

    public abstract void ping(PingMessage ping);

    public abstract void pong(PongMessage pong);

    @Override
    public void close() {
        close("");
    }

    @Override
    public void close(int code, String reason) {
        Object cr = getCloseReason(code, reason);
        try {
            doClose(cr);
        } catch (Throwable e) {
            concept.onClose(this, cr);
            concept.getEventPublisher().publish(new ConnectionCloseErrorEvent(this, cr, e));
        }
    }

    public abstract void doClose(Object reason);

    public abstract Object getCloseReason(int code, String reason);

    public abstract void doSend(Object message);
}
