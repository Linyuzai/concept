package com.github.linyuzai.connection.loadbalance.netty.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyMessageCodecAdapter;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Netty WebSocket 消息编解码适配器。
 * <p>
 * Netty WebSocket message codec adapter.
 */
public class WebSocketFrameNettyMessageCodecAdapter extends NettyMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return new WebSocketFrameMessageEncoder(encoder);
    }

    @Override
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return new WebSocketFrameMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class WebSocketFrameMessageEncoder implements MessageEncoder {

        private final MessageEncoder encoder;

        @Override
        public Object encode(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
            if (connection instanceof WebSocketNettyConnection) {
                Object encoded = encoder.encode(message, connection, concept);
                if (encoded instanceof String) {
                    return new TextWebSocketFrame((String) encoded);
                } else if (encoded instanceof byte[]) {
                    return new BinaryWebSocketFrame(Unpooled.wrappedBuffer((byte[]) encoded));
                }
            }
            return encoder.encode(message, connection, concept);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class WebSocketFrameMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            if (connection instanceof WebSocketNettyConnection) {
                if (message instanceof WebSocketFrame) {
                    if (message instanceof BinaryWebSocketFrame) {
                        return decoder.decode(((BinaryWebSocketFrame) message).content().array(),
                                connection, concept);
                    } else if (message instanceof TextWebSocketFrame) {
                        return decoder.decode(((TextWebSocketFrame) message).text(),
                                connection, concept);
                    }
                }
            }
            return decoder.decode(message, connection, concept);
        }
    }
}
