package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class JacksonTextMessageEncoder implements MessageEncoder {

    private ObjectMapper objectMapper;

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
