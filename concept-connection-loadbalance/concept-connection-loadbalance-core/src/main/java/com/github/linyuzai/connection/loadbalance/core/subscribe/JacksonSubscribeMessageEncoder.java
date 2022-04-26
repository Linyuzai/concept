package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JacksonSubscribeMessageEncoder extends JacksonTextMessageEncoder {

    public JacksonSubscribeMessageEncoder(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object getPayload(Message message) {
        return message;
    }
}
