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

    public JacksonTextMessageEncoder() {
        this(new ObjectMapper());
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
        return message.getPayload();
    }
}
