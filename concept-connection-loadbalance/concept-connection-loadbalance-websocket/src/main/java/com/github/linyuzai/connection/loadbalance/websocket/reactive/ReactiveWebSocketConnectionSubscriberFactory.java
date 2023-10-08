package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Reactive WebSocket 连接订阅器工厂。
 * <p>
 * Reactive WebSocket connection subscriber factory.
 */
@Getter
@Setter
public class ReactiveWebSocketConnectionSubscriberFactory extends WebSocketConnectionSubscriberFactory<ReactiveWebSocketConnection> {

    private ReactiveWebSocketClientFactory webSocketClientFactory;

    @Override
    public WebSocketConnectionSubscriber<ReactiveWebSocketConnection> doCreate(String scope) {
        return new ReactiveWebSocketConnectionSubscriber(webSocketClientFactory);
    }
}
