package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.TextMessage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

/**
 * 将字符串转为 {@link TextMessage} 的解码器
 * <p>
 * 用于消息转发
 */
@AllArgsConstructor
public class JacksonTextMessageDecoder implements MessageDecoder {

    private final ObjectMapper objectMapper;

    public JacksonTextMessageDecoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
        if (message instanceof String) {
            return objectMapper.readValue((String) message, TextMessage.class);
        } else if (message instanceof ByteBuffer) {
            return objectMapper.readValue(((ByteBuffer) message).array(), TextMessage.class);
        } else if (message instanceof byte[]) {
            return objectMapper.readValue((byte[]) message, TextMessage.class);
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
