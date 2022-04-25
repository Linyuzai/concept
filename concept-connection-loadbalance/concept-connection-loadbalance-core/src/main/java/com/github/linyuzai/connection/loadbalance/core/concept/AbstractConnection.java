package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    private final Map<Object, Object> metadata = new LinkedHashMap<>();

    @Setter(AccessLevel.PRIVATE)
    private String type;

    @NonNull
    private MessageEncoder messageEncoder;

    @NonNull
    private MessageDecoder messageDecoder;

    @NonNull
    private ConnectionLoadBalanceConcept concept;

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
        MessageEncoder encoder = getMessageEncoder();
        byte[] bytes = encoder.encode(message);
        doSend(bytes);
    }

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

    public abstract void doSend(byte[] bytes);
}
