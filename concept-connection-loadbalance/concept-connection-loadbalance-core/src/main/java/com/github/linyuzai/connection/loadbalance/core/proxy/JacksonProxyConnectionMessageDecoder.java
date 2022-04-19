package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonMessageEncoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class JacksonProxyConnectionMessageDecoder implements MessageDecoder {

    private ObjectMapper objectMapper;

    public JacksonProxyConnectionMessageDecoder() {
        this(new ObjectMapper().activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING));
    }

    @SneakyThrows
    @Override
    public Message decode(byte[] message) {
        return objectMapper.readValue(message, Message.class);
    }
}
