package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseMessageCodecAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用于 {@link ReactiveSseConnection} 的消息编解码适配器。
 * <p>
 * Message codec adapter for {@link ReactiveSseConnection}.
 */
public class ReactiveSseMessageCodecAdapter extends SseMessageCodecAdapter {

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new ReactiveMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ReactiveMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            return decoder.decode(message, connection, concept);
        }
    }
}
