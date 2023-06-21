package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapterFactory;

public class JavaxWebSocketMessageCodecAdapterFactory extends WebSocketMessageCodecAdapterFactory {

    @Override
    public MessageCodecAdapter create(String scope) {
        return new JavaxWebSocketMessageCodecAdapter();
    }
}
