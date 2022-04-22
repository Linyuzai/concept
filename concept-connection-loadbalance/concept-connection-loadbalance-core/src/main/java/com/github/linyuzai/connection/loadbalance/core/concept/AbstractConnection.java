package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractConnection implements Connection {

    private final Map<Object, Object> metadata = new LinkedHashMap<>();

    @NonNull
    private MessageEncoder messageEncoder;

    @NonNull
    private MessageDecoder messageDecoder;

    public AbstractConnection(Map<Object, Object> metadata) {
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
