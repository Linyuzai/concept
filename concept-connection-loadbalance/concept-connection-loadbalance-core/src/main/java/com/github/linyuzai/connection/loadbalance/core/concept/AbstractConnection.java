package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    private final Map<Object, Object> metadata = new LinkedHashMap<>();

    private final String type;

    @NonNull
    private MessageEncoder messageEncoder;

    @NonNull
    private MessageDecoder messageDecoder;

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

    public abstract void doSend(byte[] bytes);
}
