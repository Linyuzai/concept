package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.message.BinaryPingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapter;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketMessage;

/**
 * 用于 {@link ReactiveWebSocketConnection} 的消息编解码适配器
 */
public class ReactiveWebSocketMessageCodecAdapter extends WebSocketMessageCodecAdapter {

    @Override
    public MessageDecoder getClientMessageDecoder() {
        return new ReactiveMessageDecoder(super.getClientMessageDecoder());
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder() {
        return new ReactiveMessageDecoder(super.getSubscribeMessageDecoder());
    }

    @Override
    public MessageDecoder getForwardMessageDecoder() {
        return new ReactiveMessageDecoder(super.getForwardMessageDecoder());
    }

    @AllArgsConstructor
    public static class ReactiveMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message) {
            if (message instanceof WebSocketMessage) {
                WebSocketMessage.Type type = ((WebSocketMessage) message).getType();
                if (type == WebSocketMessage.Type.TEXT) {
                    return decoder.decode(((WebSocketMessage) message).getPayloadAsText());
                } else if (type == WebSocketMessage.Type.PING) {
                    return new BinaryPingMessage(((WebSocketMessage) message).getPayload().asByteBuffer());
                } else if (type == WebSocketMessage.Type.PONG) {
                    return new BinaryPongMessage(((WebSocketMessage) message).getPayload().asByteBuffer());
                } else if (type == WebSocketMessage.Type.BINARY) {
                    return decoder.decode(((WebSocketMessage) message).getPayload().asByteBuffer());
                }
            }
            return decoder.decode(message);
        }
    }
}
