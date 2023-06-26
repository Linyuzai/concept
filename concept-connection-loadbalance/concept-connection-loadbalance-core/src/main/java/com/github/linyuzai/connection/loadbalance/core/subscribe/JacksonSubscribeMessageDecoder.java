package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeException;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

/**
 * 连接订阅消息解码器
 */
@Getter
@AllArgsConstructor
public class JacksonSubscribeMessageDecoder implements MessageDecoder {

    private ObjectMapper objectMapper;

    public JacksonSubscribeMessageDecoder() {
        /*this(new ObjectMapper()
                .registerModule(new SimpleModule()
                        .addAbstractTypeMapping(ConnectionServer.class, ConnectionServerImpl.class)));*/
        this(new ObjectMapper());
    }

    @Override
    public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
        return new SubscribeMessage(parse(message));
    }

    @SneakyThrows
    public ConnectionServer parse(Object message) {
        if (message instanceof String) {
            return objectMapper.readValue((String) message, ConnectionServerImpl.class);
        } else if (message instanceof byte[]) {
            return objectMapper.readValue((byte[]) message, ConnectionServerImpl.class);
        } else if (message instanceof ByteBuffer) {
            return objectMapper.readValue(((ByteBuffer) message).array(), ConnectionServerImpl.class);
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
