package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;

@ClientEndpoint
public class JavaxWebSocketSubscriberEndpoint {

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId(), Connection.Type.SUBSCRIBER, reason);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), Connection.Type.SUBSCRIBER, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), Connection.Type.SUBSCRIBER, e);
    }
}
