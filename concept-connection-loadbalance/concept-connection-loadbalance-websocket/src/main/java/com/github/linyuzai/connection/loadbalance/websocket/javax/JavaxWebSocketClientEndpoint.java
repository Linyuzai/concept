package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;

@ClientEndpoint
public class JavaxWebSocketClientEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        WebSocketLoadBalanceConcept.getInstance().open(session, null, Connection.Type.SUBSCRIBER);
    }

    @OnClose
    public void onClose(Session session) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId(), Connection.Type.SUBSCRIBER);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), message, Connection.Type.SUBSCRIBER);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), e, Connection.Type.SUBSCRIBER);
    }
}
