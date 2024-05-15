package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;

/**
 * Javax WebSocket 连接订阅器工厂。
 * <p>
 * Javax WebSocket connection subscriber factory.
 */
@Deprecated
public class JavaxWebSocketConnectionSubscriberFactory
        extends WebSocketConnectionSubscriberFactory<JavaxWebSocketConnection> {

    @Override
    public WebSocketConnectionSubscriber<JavaxWebSocketConnection> doCreate(String scope) {
        return new JavaxWebSocketConnectionSubscriber(JavaxWebSocketSubscriberEndpoint.class);
    }
}
