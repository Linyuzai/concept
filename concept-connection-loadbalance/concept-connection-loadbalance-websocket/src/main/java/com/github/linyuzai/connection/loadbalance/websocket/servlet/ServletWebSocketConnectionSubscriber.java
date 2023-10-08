package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link ServletWebSocketConnection} 的连接订阅者。
 * <p>
 * {@link ServletWebSocketConnection} connection subscriber.
 */
@Getter
@RequiredArgsConstructor
public class ServletWebSocketConnectionSubscriber extends
        WebSocketConnectionSubscriber<ServletWebSocketConnection> {

    private final ServletWebSocketClientFactory webSocketClientFactory;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept,
                            Consumer<ServletWebSocketConnection> onSuccess,
                            Consumer<Throwable> onError,
                            Runnable onComplete) {
        try {
            WebSocketClient client = webSocketClientFactory.create();
            ServletWebSocketSubscriberHandler handler = new ServletWebSocketSubscriberHandler(concept, session -> {
                ServletWebSocketConnection connection = new ServletWebSocketConnection(session);
                connection.setType(Connection.Type.SUBSCRIBER);
                onSuccess.accept(connection);
            });
            WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, uri.toString());
            manager.start();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public String getType() {
        return "servlet";
    }
}
