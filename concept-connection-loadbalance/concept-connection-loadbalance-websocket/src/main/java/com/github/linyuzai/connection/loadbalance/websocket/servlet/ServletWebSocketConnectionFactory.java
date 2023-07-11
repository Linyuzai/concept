package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * {@link ServletWebSocketConnection} 连接工厂
 */
public class ServletWebSocketConnectionFactory extends WebSocketConnectionFactory<ServletWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return o instanceof WebSocketSession;
    }

    @Override
    protected AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept) {
        return new ServletWebSocketConnection((WebSocketSession) o);
    }
}
