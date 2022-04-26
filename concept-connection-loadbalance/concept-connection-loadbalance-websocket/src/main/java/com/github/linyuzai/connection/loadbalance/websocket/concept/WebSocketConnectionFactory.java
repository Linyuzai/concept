package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;

public abstract class WebSocketConnectionFactory<T extends WebSocketConnection>
        extends AbstractConnectionFactory<T, WebSocketLoadBalanceConcept> {
}
