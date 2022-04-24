package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class JacksonSubscribeMessageDecoder implements MessageDecoder {

    private ObjectMapper objectMapper;

    public JacksonSubscribeMessageDecoder() {
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
