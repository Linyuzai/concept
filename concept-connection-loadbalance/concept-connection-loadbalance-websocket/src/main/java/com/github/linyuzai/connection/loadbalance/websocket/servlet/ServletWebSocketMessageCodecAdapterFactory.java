package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketMessageCodecAdapterFactory;

public class ServletWebSocketMessageCodecAdapterFactory extends WebSocketMessageCodecAdapterFactory {

    @Override
    public MessageCodecAdapter create(String scope) {
        return new ServletWebSocketMessageCodecAdapter();
    }
}
