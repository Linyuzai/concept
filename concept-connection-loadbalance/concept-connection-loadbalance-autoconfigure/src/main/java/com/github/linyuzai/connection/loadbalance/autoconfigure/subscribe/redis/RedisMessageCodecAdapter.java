package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Redis 消息编解码适配器。
 * <p>
 * {@link MessageCodecAdapter} for Redis.
 */
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
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            if (message instanceof org.springframework.data.redis.connection.Message) {
                return decoder.decode(((org.springframework.data.redis.connection.Message) message).getBody(), connection, concept);
            }
            return decoder.decode(message, connection, concept);
        }
    }
}
