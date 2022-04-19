package com.github.linyuzai.connection.loadbalance.websocket.standard;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import java.util.Map;

public interface WebSocketEndpoint {

    default void add(Session session, Map<String, String> metadata) {
        WebSocketLoadBalanceConcept.getInstance().add(session, metadata);
    }

    default void remove(Session session) {
        WebSocketLoadBalanceConcept.getInstance().remove(session.getId());
    }

    default void message(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), message);
    }

    default void error(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), e);
    }
}
