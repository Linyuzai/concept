package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.Map;

/**
 * {@link ReactiveWebSocketConnection} 连接工厂。
 * <p>
 * {@link ReactiveWebSocketConnection} connection factory.
 */
public class ReactiveWebSocketConnectionFactory extends WebSocketConnectionFactory<ReactiveWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return o instanceof Object[] &&
                ((Object[]) o).length == 2 &&
                ((Object[]) o)[0] instanceof WebSocketSession &&
                ((Object[]) o)[1] instanceof FluxSink;
    }

    @Override
    protected AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept) {
        WebSocketSession session = (WebSocketSession) ((Object[]) o)[0];
        @SuppressWarnings("unchecked")
        FluxSink<WebSocketMessage> sender = (FluxSink<WebSocketMessage>) ((Object[]) o)[1];
        return new ReactiveWebSocketConnection(session, sender);
    }
}
