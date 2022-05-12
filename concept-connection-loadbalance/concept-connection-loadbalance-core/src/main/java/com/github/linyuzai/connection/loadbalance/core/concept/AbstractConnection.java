package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionCloseErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.event.TypeRedefineEvent;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    protected final Map<Object, Object> metadata = new LinkedHashMap<>();

    @Setter(AccessLevel.PRIVATE)
    protected String type;

    @NonNull
    protected MessageEncoder messageEncoder;

    @NonNull
    protected MessageDecoder messageDecoder;

    @NonNull
    protected ConnectionLoadBalanceConcept concept;

    protected boolean alive;

    protected long lastHeartbeat;

    public AbstractConnection(String type) {
        this(type, null);
    }

    public AbstractConnection(String type, Map<Object, Object> metadata) {
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
    public void redefineType(String type, Redefiner redefiner) {
        if (this.type.equals(type)) {
            return;
        }
        String oldType = this.type;
        concept.move(getId(), this.type, type, connection -> {
            AbstractConnection.this.type = type;
            if (redefiner != null) {
                redefiner.onRedefine();
            }
        });
        concept.publish(new TypeRedefineEvent(this, oldType));
    }

    @Override
    public void close() {
        try {
            doClose();
        } catch (Throwable e) {
            concept.publish(new ConnectionCloseErrorEvent(this, e));
        }
    }

    public abstract void doClose() throws IOException;

    public abstract void doSend(Object message);
}
