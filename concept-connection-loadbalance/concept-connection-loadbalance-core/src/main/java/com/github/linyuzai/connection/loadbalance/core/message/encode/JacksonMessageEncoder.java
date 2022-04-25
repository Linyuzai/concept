package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class JacksonMessageEncoder implements MessageEncoder {

    private ObjectMapper objectMapper;

    public JacksonMessageEncoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public byte[] encode(Message message) {
        Object payload = getPayload(message);
        return objectMapper.writeValueAsBytes(payload);
    }

    public Object getPayload(Message message) {
        return message.getPayload();
    }
}
