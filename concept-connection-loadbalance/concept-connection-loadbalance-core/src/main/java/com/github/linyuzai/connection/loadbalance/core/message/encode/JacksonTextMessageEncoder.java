package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 将消息转为 json 的编码器。
 * <p>
 * Encode message to json.
 */
@Getter
@RequiredArgsConstructor
public class JacksonTextMessageEncoder implements MessageEncoder {

    private final ObjectMapper objectMapper;

    /**
     * 用于设置对 {@link Message} 进行编码还是对 {@link Message#getPayload()} 进行编码。
     * <p>
     * Used for encoding {@link Message} or {@link Message#getPayload()}.
     */
    private final boolean messageAsPayload;

    public JacksonTextMessageEncoder() {
        this(new ObjectMapper(), false);
    }

    public JacksonTextMessageEncoder(boolean messageAsPayload) {
        this(new ObjectMapper(), messageAsPayload);
    }

    public JacksonTextMessageEncoder(ObjectMapper objectMapper) {
        this(objectMapper, false);
    }

    @SneakyThrows
    @Override
    public Object encode(Message message, ConnectionLoadBalanceConcept concept) {
        Object payload = getPayload(message);
        if (payload instanceof String || payload instanceof byte[]) {
            return payload;
        }
        return objectMapper.writeValueAsString(payload);
    }

    public Object getPayload(Message message) {
        if (messageAsPayload) {
            return message;
        } else {
            return message.getPayload();
        }
    }
}
