package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.redis;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReactiveSubscription;

/**
 * Reactive Redis 消息编解码适配器。
 * <p>
 * {@link MessageCodecAdapter} for Reactive Redis.
 */
public class ReactiveRedisMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new ReactiveRedisMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ReactiveRedisMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
            if (message instanceof ReactiveSubscription.Message) {
                return decoder.decode(((ReactiveSubscription.Message<?, ?>) message).getMessage(), concept);
            }
            return decoder.decode(message, concept);
        }
    }
}
