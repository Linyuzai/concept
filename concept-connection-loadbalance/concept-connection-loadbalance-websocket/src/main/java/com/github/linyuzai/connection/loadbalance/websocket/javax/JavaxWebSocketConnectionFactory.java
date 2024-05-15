package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;

import javax.websocket.Session;
import java.util.Map;

/**
 * {@link JavaxWebSocketConnection} 连接工厂。
 * <p>
 * {@link JavaxWebSocketConnection} connection factory.
 */
@Deprecated
public class JavaxWebSocketConnectionFactory extends WebSocketConnectionFactory<JavaxWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return o instanceof Session;
    }

    @Override
    protected AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept) {
        return new JavaxWebSocketConnection((Session) o);
    }
}
