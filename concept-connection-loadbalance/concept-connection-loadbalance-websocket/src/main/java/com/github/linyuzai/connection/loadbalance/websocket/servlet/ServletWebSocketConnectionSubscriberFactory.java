package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;

/**
 * {@link ServletWebSocketConnection} 订阅者工厂。
 * <p>
 * {@link ServletWebSocketConnection} subscriber factory.
 */
public class ServletWebSocketConnectionSubscriberFactory extends WebSocketConnectionSubscriberFactory<ServletWebSocketConnection> {

    @Override
    public WebSocketConnectionSubscriber<ServletWebSocketConnection> doCreate(String scope) {
        return new ServletWebSocketConnectionSubscriber();
    }
}
