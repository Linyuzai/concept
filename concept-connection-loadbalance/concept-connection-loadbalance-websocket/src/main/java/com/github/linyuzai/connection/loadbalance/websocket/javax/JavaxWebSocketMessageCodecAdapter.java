package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.message.BinaryPongMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapter;
import lombok.AllArgsConstructor;

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
        public Message decode(Object message) {
            if (message instanceof javax.websocket.PongMessage) {
                return new BinaryPongMessage(((javax.websocket.PongMessage) message).getApplicationData());
            }
            return decoder.decode(message);
        }
    }
}
