package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseMessageCodecAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用于 {@link ServletSseConnection} 的消息编解码适配器。
 * <p>
 * Message codec adapter for {@link ServletSseConnection}.
 */
public class ServletSseMessageCodecAdapter extends SseMessageCodecAdapter {

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new ServletMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ServletMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            return decoder.decode(message, connection, concept);
        }
    }
}
