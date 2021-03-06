package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * {@link ServletWebSocketConnection} 连接工厂
 */
public class ServletWebSocketConnectionFactory extends WebSocketConnectionFactory<ServletWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof WebSocketSession;
    }

    @Override
    public ServletWebSocketConnection doCreate(Object o, Map<Object, Object> metadata, WebSocketLoadBalanceConcept concept) {
        WebSocketSession session = (WebSocketSession) o;
        return new ServletWebSocketConnection(session, Connection.Type.CLIENT, metadata);
    }
}
