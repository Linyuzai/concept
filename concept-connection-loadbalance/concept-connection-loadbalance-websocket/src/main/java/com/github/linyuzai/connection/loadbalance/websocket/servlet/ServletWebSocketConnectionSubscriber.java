package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
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

    public ServletWebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    @Override
    public void doSubscribe(URI uri, WebSocketLoadBalanceConcept concept, Consumer<ServletWebSocketConnection> consumer) {
        WebSocketClient client = newWebSocketClient();
        ServletWebSocketSubscriberHandler handler = new ServletWebSocketSubscriberHandler(concept, session ->
                consumer.accept(new ServletWebSocketConnection(session, Connection.Type.SUBSCRIBER)));
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, uri.toString());
        manager.start();
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
