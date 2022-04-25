package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.exception.WebSocketLoadBalanceException;
import org.springframework.util.ClassUtils;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.util.function.Consumer;

public class ServletWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber {

    private static final boolean javaxPresent;

    private static final boolean jettyPresent;

    static {
        ClassLoader loader = ServletWebSocketConnectionSubscriber.class.getClassLoader();
        javaxPresent = ClassUtils.isPresent("org.apache.tomcat.websocket.server.WsHttpUpgradeHandler", loader);
        jettyPresent = ClassUtils.isPresent("org.eclipse.jetty.websocket.client.WebSocketClient", loader);
    }

    @Override
    public void doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept, Consumer<Connection> consumer) {
        WebSocketClient client = newWebSocketClient();
        ServletWebSocketSubscriberHandler handler = new ServletWebSocketSubscriberHandler(concept);
        URI uri = getUri(server);
        WebSocketConnectionManager manager =
                new WebSocketConnectionManager(client, handler, uri.toString());
        manager.start();
        WebSocketSession session = handler.getSession();
        ServletWebSocketConnection connection =
                new ServletWebSocketConnection(session, Connection.Type.SUBSCRIBER);
        connection.getMetadata().put(ConnectionServer.class, server);
        setDefaultMessageEncoder(connection);
        setDefaultMessageDecoder(connection);
        consumer.accept(connection);
    }

    public WebSocketClient newWebSocketClient() {
        if (jettyPresent) {
            return new JettyWebSocketClient();
        } else if (javaxPresent) {
            return new StandardWebSocketClient();
        } else {
            throw new WebSocketLoadBalanceException("No suitable client found");
        }
    }

    @Override
    public String getType() {
        return "servlet";
    }
}
