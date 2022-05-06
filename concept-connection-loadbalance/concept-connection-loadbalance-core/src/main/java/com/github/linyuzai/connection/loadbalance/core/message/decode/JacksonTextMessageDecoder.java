package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.TextMessage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class JacksonTextMessageDecoder implements MessageDecoder {

    private final ObjectMapper objectMapper;

    public JacksonTextMessageDecoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Message decode(Object message) {
        if (message instanceof String) {
            return objectMapper.readValue((String) message, TextMessage.class);
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
