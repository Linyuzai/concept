package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeException;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

@Getter
@AllArgsConstructor
public class JacksonSubscribeMessageDecoder implements MessageDecoder {

    private ObjectMapper objectMapper;

    public JacksonSubscribeMessageDecoder() {
        this(new ObjectMapper()
                .registerModule(new SimpleModule()
                        .addAbstractTypeMapping(ConnectionServer.class, ConnectionServerImpl.class)));
    }

    @SneakyThrows
    @Override
    public Message decode(Object message) {
        if (message instanceof String) {
            return objectMapper.readValue((String) message, SubscribeMessage.class);
        } else if (message instanceof byte[]) {
            return objectMapper.readValue((byte[]) message, SubscribeMessage.class);
        } else if (message instanceof ByteBuffer) {
            return objectMapper.readValue(((ByteBuffer) message).array(), SubscribeMessage.class);
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
