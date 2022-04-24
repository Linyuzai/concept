package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

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
    public Message decode(byte[] message) {
        return objectMapper.readValue(message, SubscribeMessage.class);
    }
}
