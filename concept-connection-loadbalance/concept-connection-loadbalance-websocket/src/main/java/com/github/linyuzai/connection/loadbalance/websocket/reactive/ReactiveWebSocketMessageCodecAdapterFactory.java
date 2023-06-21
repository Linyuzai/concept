package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapterFactory;

public class ReactiveWebSocketMessageCodecAdapterFactory extends WebSocketMessageCodecAdapterFactory {

    @Override
    public MessageCodecAdapter create(String scope) {
        return new ReactiveWebSocketMessageCodecAdapter();
    }
}
