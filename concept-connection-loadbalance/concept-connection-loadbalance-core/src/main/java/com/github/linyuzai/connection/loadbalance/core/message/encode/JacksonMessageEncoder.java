package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

/**
 * 将消息转为 json 的编码器。
 * <p>
 * Encode message to json.
 */
@Getter
@RequiredArgsConstructor
public class JacksonMessageEncoder extends AbstractMessageEncoder {

    private final ObjectMapper objectMapper;

    public JacksonMessageEncoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Object doEncode(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
        Object payload = getPayload(message);
        if (payload instanceof String || payload instanceof byte[] || payload instanceof ByteBuffer) {
            return payload;
        }
        return objectMapper.writeValueAsString(payload);
    }

    public Object getPayload(Message message) {
        return message.getPayload();
    }
}
