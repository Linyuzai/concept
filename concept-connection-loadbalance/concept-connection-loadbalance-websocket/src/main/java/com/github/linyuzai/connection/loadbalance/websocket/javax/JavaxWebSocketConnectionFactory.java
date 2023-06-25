package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;

import javax.websocket.Session;
import java.util.Map;

/**
 * {@link JavaxWebSocketConnection} 连接工厂
 */
public class JavaxWebSocketConnectionFactory extends WebSocketConnectionFactory<JavaxWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof Session;
    }

    @Override
    public JavaxWebSocketConnection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        Session session = (Session) o;
        return new JavaxWebSocketConnection(session, Connection.Type.CLIENT, metadata);
    }
}
