package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.BinaryPingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapter;
import lombok.AllArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;

/**
 * 用于 {@link ServletWebSocketConnection} 的消息编解码适配器
 */
public class ServletWebSocketMessageCodecAdapter extends WebSocketMessageCodecAdapter {

    @Override
    public MessageDecoder getClientMessageDecoder() {
        return new ServletMessageDecoder(super.getClientMessageDecoder());
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder() {
        return new ServletMessageDecoder(super.getSubscribeMessageDecoder());
    }

    @Override
    public MessageDecoder getForwardMessageDecoder() {
        return new ServletMessageDecoder(super.getForwardMessageDecoder());
    }

    @AllArgsConstructor
    public static class ServletMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message) {
            if (message instanceof WebSocketMessage) {
                if (message instanceof org.springframework.web.socket.TextMessage) {
                    return decoder.decode(((org.springframework.web.socket.TextMessage) message).getPayload());
                } else if (message instanceof org.springframework.web.socket.PingMessage) {
                    return new BinaryPingMessage(((org.springframework.web.socket.PingMessage) message).getPayload());
                } else if (message instanceof org.springframework.web.socket.PongMessage) {
                    return new BinaryPongMessage(((org.springframework.web.socket.PongMessage) message).getPayload());
                } else if (message instanceof org.springframework.web.socket.BinaryMessage) {
                    return decoder.decode(((org.springframework.web.socket.BinaryMessage) message).getPayload());
                }
            }
            return decoder.decode(message);
        }
    }
}
