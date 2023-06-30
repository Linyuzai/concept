package com.github.linyuzai.connection.loadbalance.autoconfigure.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class RedisMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new RedisMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class RedisMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
            if (message instanceof org.springframework.data.redis.connection.Message) {
                return decoder.decode(((org.springframework.data.redis.connection.Message) message).getBody(), concept);
            }
            return decoder.decode(message, concept);
        }
    }
}
