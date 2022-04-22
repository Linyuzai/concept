package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;

public class ServletWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber {

    @Override
    public Connection doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        ServletSubscribeWebSocketHandler handler = new ServletSubscribeWebSocketHandler(concept, server);
        URI uri = getUri(server);
        WebSocketConnectionManager manager =
                new WebSocketConnectionManager(client, handler, uri.toString());
        manager.startInternal();
        return handler.getConnection();
    }

    @Override
    public String getType() {
        return "servlet";
    }
}
