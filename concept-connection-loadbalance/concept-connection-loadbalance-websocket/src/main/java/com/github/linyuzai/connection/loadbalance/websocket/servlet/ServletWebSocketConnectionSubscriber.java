package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.javax.ContainerWebSocketConnectionSubscriber;
import lombok.NoArgsConstructor;
import org.springframework.util.ClassUtils;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.util.function.Consumer;

/**
 * {@link ServletWebSocketConnection} 的连接订阅者
 */
@NoArgsConstructor
public class ServletWebSocketConnectionSubscriber extends
        ContainerWebSocketConnectionSubscriber<ServletWebSocketConnection> {

    private static final boolean jettyPresent;

    static {
        ClassLoader loader = ServletWebSocketConnectionSubscriber.class.getClassLoader();
        jettyPresent = ClassUtils.isPresent("org.eclipse.jetty.websocket.client.WebSocketClient", loader);
    }

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept,
                            Consumer<ServletWebSocketConnection> onSuccess,
                            Consumer<Throwable> onError,
                            Runnable onComplete) {
        try {
            WebSocketClient client = newWebSocketClient();
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

    public WebSocketClient newWebSocketClient() {
        if (jettyPresent) {
            return new JettyWebSocketClient();
        } else {
            return new StandardWebSocketClient(getContainer());
        }
    }

    @Override
    public String getType() {
        return "servlet";
    }
}
