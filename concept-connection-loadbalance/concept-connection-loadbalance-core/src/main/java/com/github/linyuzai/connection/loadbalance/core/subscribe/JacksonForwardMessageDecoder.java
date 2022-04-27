package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.TextMessage;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeException;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

@Getter
@AllArgsConstructor
public class JacksonForwardMessageDecoder implements MessageDecoder {

    private ObjectMapper objectMapper;

    public JacksonForwardMessageDecoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Message decode(Object message) {
        if (message instanceof String) {
            return new TextMessage((String) message);
        } else if (message instanceof byte[]) {
            return new BinaryMessage(ByteBuffer.wrap((byte[]) message));
        } else if (message instanceof ByteBuffer) {
            return new BinaryMessage(((ByteBuffer) message));
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
