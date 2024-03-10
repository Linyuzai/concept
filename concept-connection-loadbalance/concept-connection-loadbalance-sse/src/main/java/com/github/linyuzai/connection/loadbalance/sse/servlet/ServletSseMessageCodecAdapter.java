package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseMessageCodecAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

/**
 * 用于 {@link ServletSseConnection} 的消息编解码适配器。
 * <p>
 * Message codec adapter for {@link ServletSseConnection}.
 */
public class ServletSseMessageCodecAdapter extends SseMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return new ServletSseMessageEncoder(encoder);
    }

    @Override
    public MessageEncoder getForwardMessageEncoder(MessageEncoder encoder) {
        return new ServletSseMessageEncoder(encoder);
    }

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new ServletSseMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ServletSseMessageEncoder implements MessageEncoder {

        private final MessageEncoder encoder;

        @Override
        public Object encode(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
            Object encoded = encoder.encode(message, connection, concept);
            if (encoded instanceof byte[]) {
                return new String((byte[]) encoded);
            } else if (encoded instanceof ByteBuffer) {
                return new String(((ByteBuffer) encoded).array());
            } else {
                return encoded;
            }
        }
    }

    @Deprecated
    @Getter
    @RequiredArgsConstructor
    public static class ServletSseMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            return decoder.decode(message, connection, concept);
        }
    }
}
