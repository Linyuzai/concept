package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;

public class ServletWebSocketConnectionSubscriberFactory extends WebSocketConnectionSubscriberFactory<ServletWebSocketConnection> {

    @Override
    public WebSocketConnectionSubscriber<ServletWebSocketConnection> doCreate(String scope) {
        return new ServletWebSocketConnectionSubscriber();
    }
}
