package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceException;
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
public class ServletWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber<ServletWebSocketConnection> {

    private static final boolean javaxPresent;

    private static final boolean jettyPresent;

    static {
        ClassLoader loader = ServletWebSocketConnectionSubscriber.class.getClassLoader();
        javaxPresent = ClassUtils.isPresent("org.apache.tomcat.websocket.server.WsHttpUpgradeHandler", loader);
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
        } else if (javaxPresent) {
            //TODO WebSocketContainer
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
