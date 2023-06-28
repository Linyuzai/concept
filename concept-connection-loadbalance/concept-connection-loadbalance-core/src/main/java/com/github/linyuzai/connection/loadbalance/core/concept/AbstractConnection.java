package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncodeException;
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
import java.util.function.Consumer;

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
        send(message, () -> {

        }, e ->
                concept.getEventPublisher()
                        .publish(new MessageSendErrorEvent(this, message, e)));
    }

    @Override
    public void send(@NonNull Message message, Runnable success, Consumer<Throwable> error) {
        if (message instanceof PingMessage) {
            ping((PingMessage) message, success, error);
        } else if (message instanceof PongMessage) {
            pong((PongMessage) message, success, error);
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
                throw new MessageEncodeException(message, e);
            }

            Consumer<Consumer<Throwable>> send = consumer -> doSend(encode, success, consumer);
            send.accept(e -> messageRetryStrategy.retry(e, send, error));
        }
    }

    public void ping(PingMessage message) {
        ping(message, () -> concept.getEventPublisher()
                        .publish(new MessageSendSuccessEvent(this, message)),
                e -> concept.getEventPublisher()
                        .publish(new MessageSendErrorEvent(this, message, e)));
    }

    public void ping(PingMessage message, Runnable success, Consumer<Throwable> error) {
        Consumer<Consumer<Throwable>> ping = consumer -> doPing(message, success, consumer);
        ping.accept(e -> messageRetryStrategy.retry(e, ping, error));
    }

    public void pong(PongMessage message) {
        pong(message, () -> concept.getEventPublisher()
                        .publish(new MessageSendSuccessEvent(this, message)),
                e -> concept.getEventPublisher()
                        .publish(new MessageSendErrorEvent(this, message, e)));
    }

    public void pong(PongMessage message, Runnable success, Consumer<Throwable> error) {
        Consumer<Consumer<Throwable>> pong = consumer -> doPong(message, success, consumer);
        pong.accept(e -> messageRetryStrategy.retry(e, pong, error));
    }

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

    public abstract void doSend(Object message, Runnable success, Consumer<Throwable> error);

    public abstract void doPing(PingMessage message, Runnable success, Consumer<Throwable> error);

    public abstract void doPong(PongMessage message, Runnable success, Consumer<Throwable> error);
}
