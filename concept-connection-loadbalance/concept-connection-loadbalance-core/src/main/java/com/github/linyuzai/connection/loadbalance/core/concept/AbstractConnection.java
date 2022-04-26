package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

    public AbstractConnection(String type) {
        this.type = type;
    }

    public AbstractConnection(String type, Map<Object, Object> metadata) {
        this.type = type;
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    @Override
    public void send(Message message) {
        if (message instanceof PingMessage) {
            Object payload = message.getPayload();
            Object ping = payload instanceof String ?
                    ((String) payload).getBytes(StandardCharsets.UTF_8)
                    : payload;
            if (payloadSupportPingOrPong(ping)) {
                ping(ping);
            } else {
                throw new ConnectionLoadBalanceException("Can not send ping " + payload);
            }
        } else if (message instanceof PongMessage) {
            Object payload = message.getPayload();
            Object pong = payload instanceof String ?
                    ((String) payload).getBytes(StandardCharsets.UTF_8)
                    : payload;
            if (payloadSupportPingOrPong(pong)) {
                pong(pong);
            } else {
                throw new ConnectionLoadBalanceException("Can not send pong " + payload);
            }
        } else {
            MessageEncoder encoder = getMessageEncoder();
            Object encode = encoder.encode(message);
            doSend(encode);
        }
    }

    public boolean payloadSupportPingOrPong(Object payload) {
        return payload instanceof byte[] || payload instanceof ByteBuffer;
    }

    public abstract void ping(Object ping);

    public abstract void pong(Object pong);

    @Override
    public void redefineType(String type, Redefiner redefiner) {
        if (this.type.equals(type)) {
            return;
        }
        concept.move(getId(), this.type, type, connection -> {
            AbstractConnection.this.type = type;
            if (redefiner != null) {
                redefiner.onRedefine();
            }
        });
    }

    public abstract void doSend(Object message);
}
