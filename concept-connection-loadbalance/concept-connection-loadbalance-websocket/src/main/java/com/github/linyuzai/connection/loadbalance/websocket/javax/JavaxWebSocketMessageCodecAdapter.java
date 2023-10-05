package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用于 {@link JavaxWebSocketConnection} 的消息编解码适配器。
 * <p>
 * Message codec adapter for {@link JavaxWebSocketConnection}.
 */
public class JavaxWebSocketMessageCodecAdapter extends WebSocketMessageCodecAdapter {

    @Override
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return new JavaxMessageDecoder(decoder);
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder(MessageDecoder decoder) {
        return new JavaxMessageDecoder(decoder);
    }

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new JavaxMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class JavaxMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            if (message instanceof javax.websocket.PongMessage) {
                return new BinaryPongMessage(((javax.websocket.PongMessage) message).getApplicationData());
            }
            return decoder.decode(message, connection, concept);
        }
    }
}
