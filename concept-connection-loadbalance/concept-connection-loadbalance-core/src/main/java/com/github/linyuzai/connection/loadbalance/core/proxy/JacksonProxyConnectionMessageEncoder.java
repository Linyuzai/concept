package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;

public class JacksonProxyConnectionMessageEncoder extends JacksonMessageEncoder {

    public JacksonProxyConnectionMessageEncoder(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public JacksonProxyConnectionMessageEncoder() {
        this(new ObjectMapper().activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING));
    }

    @Override
    public Object getPayload(Message message) {
        return message;
    }
}
