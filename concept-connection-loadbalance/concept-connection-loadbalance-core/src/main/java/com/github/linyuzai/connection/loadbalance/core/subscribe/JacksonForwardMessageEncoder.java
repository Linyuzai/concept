package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JacksonForwardMessageEncoder extends JacksonTextMessageEncoder {

    public JacksonForwardMessageEncoder(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object getPayload(Message message) {
        return message;
    }
}
