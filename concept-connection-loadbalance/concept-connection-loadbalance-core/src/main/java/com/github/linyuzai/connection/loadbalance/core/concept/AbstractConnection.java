package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 连接抽象类
 */
@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    protected final Map<Object, Object> metadata = new LinkedHashMap<>();

    @NonNull
    protected String type;

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

    public AbstractConnection(String type) {
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
        if (!isAlive()) {
            return;
        }
        if (message instanceof PingMessage) {
            ping((PingMessage) message);
        } else if (message instanceof PongMessage) {
            pong((PongMessage) message);
        } else {
            MessageEncoder encoder = getMessageEncoder();
            Object encode = encoder.encode(message);
            doSend(encode);
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
            concept.publish(new ConnectionCloseErrorEvent(this, cr, e));
        }
    }

    public abstract void doClose(Object reason);

    public abstract Object getCloseReason(int code, String reason);

    public abstract void doSend(Object message);
}
