package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;

public class ReactiveWebSocketConnectionSubscriberFactory extends WebSocketConnectionSubscriberFactory<ReactiveWebSocketConnection> {

    @Override
    public WebSocketConnectionSubscriber<ReactiveWebSocketConnection> doCreate(String scope) {
        return new ReactiveWebSocketConnectionSubscriber();
    }
}
