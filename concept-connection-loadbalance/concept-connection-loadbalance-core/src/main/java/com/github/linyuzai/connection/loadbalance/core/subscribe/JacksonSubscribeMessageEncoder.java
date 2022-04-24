package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;

public class JacksonSubscribeMessageEncoder extends JacksonMessageEncoder {

    public JacksonSubscribeMessageEncoder(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public JacksonSubscribeMessageEncoder() {
        this(new ObjectMapper());
    }

    @Override
    public Object getPayload(Message message) {
        return message;
    }
}
