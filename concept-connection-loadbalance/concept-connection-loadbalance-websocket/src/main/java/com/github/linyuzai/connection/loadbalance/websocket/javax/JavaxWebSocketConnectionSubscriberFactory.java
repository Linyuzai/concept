package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;

public class JavaxWebSocketConnectionSubscriberFactory
        extends WebSocketConnectionSubscriberFactory<JavaxWebSocketConnection> {

    @Override
    public WebSocketConnectionSubscriber<JavaxWebSocketConnection> doCreate(String scope) {
        return new JavaxWebSocketConnectionSubscriber(JavaxWebSocketSubscriberEndpoint.class);
    }
}
