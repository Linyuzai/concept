package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link ServletWebSocketConnection} 订阅者工厂。
 * <p>
 * {@link ServletWebSocketConnection} subscriber factory.
 */
@Getter
@Setter
public class ServletWebSocketConnectionSubscriberFactory extends WebSocketConnectionSubscriberFactory<ServletWebSocketConnection> {

    private ServletWebSocketClientFactory webSocketClientFactory;

    @Override
    public WebSocketConnectionSubscriber<ServletWebSocketConnection> doCreate(String scope) {
        return new ServletWebSocketConnectionSubscriber(webSocketClientFactory);
    }
}
