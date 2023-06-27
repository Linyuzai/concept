package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapter;
import lombok.AllArgsConstructor;

/**
 * 用于 {@link JavaxWebSocketConnection} 的消息编解码适配器
 */
public class JavaxWebSocketMessageCodecAdapter extends WebSocketMessageCodecAdapter {

    @Override
    public MessageDecoder getClientMessageDecoder() {
        return new JavaxMessageDecoder(super.getClientMessageDecoder());
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder() {
        return new JavaxMessageDecoder(super.getSubscribeMessageDecoder());
    }

    @Override
    public MessageDecoder getForwardMessageDecoder() {
        return new JavaxMessageDecoder(super.getForwardMessageDecoder());
    }

    @AllArgsConstructor
    public static class JavaxMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
            if (message instanceof javax.websocket.PongMessage) {
                return new BinaryPongMessage(((javax.websocket.PongMessage) message).getApplicationData());
            }
            return decoder.decode(message);
        }
    }
}
