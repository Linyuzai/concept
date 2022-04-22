package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ProxyMarker;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.socket.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServletSubscribeWebSocketHandler extends ServletLoadBalanceWebSocketHandler {

    @Getter
    private Connection connection;

    @Getter
    private final ConnectionServer connectionServer;

    public ServletSubscribeWebSocketHandler(WebSocketLoadBalanceConcept concept, ConnectionServer server) {
        super(concept);
        this.connectionServer = server;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        Map<Object, Object> metadata = new LinkedHashMap<>();
        metadata.put(ConnectionServer.class, connectionServer);
        connection = new ServletWebSocketConnection(session, metadata);
        concept.open(connection, Connection.Type.SUBSCRIBER);
    }
}
