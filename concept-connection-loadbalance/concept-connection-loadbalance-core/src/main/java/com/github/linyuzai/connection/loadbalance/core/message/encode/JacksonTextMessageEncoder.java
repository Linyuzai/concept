package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * 将消息转为 json 的编码器
 * <p>
 * 用于发送消息给客户端
 */
@Getter
@AllArgsConstructor
public class JacksonTextMessageEncoder implements MessageEncoder {

    private ObjectMapper objectMapper;

    /**
     * 用于设置对 {@link Message} 进行编码还是对 {@link Message#getPayload()} 进行编码
     * <p>
     * 转发消息时为 true，发送消息给客户端时为 false
     */
    private boolean messageAsPayload;

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
    public String encode(Message message) {
        Object payload = getPayload(message);
        if (payload instanceof String) {
            return (String) payload;
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
