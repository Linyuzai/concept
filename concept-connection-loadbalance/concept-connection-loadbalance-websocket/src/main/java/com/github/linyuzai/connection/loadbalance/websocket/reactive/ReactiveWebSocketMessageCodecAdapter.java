package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketMessage;

/**
 * 用于 {@link ReactiveWebSocketConnection} 的消息编解码适配器。
 * <p>
 * Message codec adapter for {@link ReactiveWebSocketConnection}.
 */
public class ReactiveWebSocketMessageCodecAdapter extends WebSocketMessageCodecAdapter {

    @Override
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return new ReactiveMessageDecoder(decoder);
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder(MessageDecoder decoder) {
        return new ReactiveMessageDecoder(decoder);
    }

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
            if (message instanceof WebSocketMessage) {
                WebSocketMessage.Type type = ((WebSocketMessage) message).getType();
                if (type == WebSocketMessage.Type.TEXT) {
                    return decoder.decode(((WebSocketMessage) message).getPayloadAsText(), connection, concept);
                } else if (type == WebSocketMessage.Type.PING) {
                    return new BinaryPingMessage(((WebSocketMessage) message).getPayload().asByteBuffer());
                } else if (type == WebSocketMessage.Type.PONG) {
                    return new BinaryPongMessage(((WebSocketMessage) message).getPayload().asByteBuffer());
                } else if (type == WebSocketMessage.Type.BINARY) {
                    return decoder.decode(((WebSocketMessage) message).getPayload().asByteBuffer(), connection, concept);
                }
            }
            return decoder.decode(message, connection, concept);
        }
    }
}
