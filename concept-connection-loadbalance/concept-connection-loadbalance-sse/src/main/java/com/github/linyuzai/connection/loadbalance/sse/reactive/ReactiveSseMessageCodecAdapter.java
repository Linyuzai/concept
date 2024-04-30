package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseMessageCodecAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;

import java.nio.ByteBuffer;

/**
 * 用于 {@link ReactiveSseConnection} 的消息编解码适配器。
 * <p>
 * Message codec adapter for {@link ReactiveSseConnection}.
 */
public class ReactiveSseMessageCodecAdapter extends SseMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return new ReactiveSseMessageEncoder(encoder);
    }

    @Override
    public MessageEncoder getForwardMessageEncoder(MessageEncoder encoder) {
        return new ReactiveSseMessageEncoder(encoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ReactiveSseMessageEncoder implements MessageEncoder {

        private final MessageEncoder encoder;

        @Override
        public Object encode(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
            Object encoded = encoder.encode(message, connection, concept);
            if (encoded instanceof ServerSentEvent) {
                return encoded;
            }
            if (encoded instanceof byte[]) {
                return ServerSentEvent.builder(new String((byte[]) encoded)).build();
            } else if (encoded instanceof ByteBuffer) {
                return ServerSentEvent.builder(new String(((ByteBuffer) encoded).array())).build();
            } else {
                return ServerSentEvent.builder(encoded).build();
            }
        }
    }
}
