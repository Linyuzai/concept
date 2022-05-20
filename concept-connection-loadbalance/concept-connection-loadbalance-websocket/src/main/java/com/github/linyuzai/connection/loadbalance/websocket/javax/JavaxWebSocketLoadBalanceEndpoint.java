package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;

@ServerEndpoint(WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT)
public class JavaxWebSocketLoadBalanceEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        JavaxWebSocketConnection connection = new JavaxWebSocketConnection(session, Connection.Type.OBSERVABLE);
        WebSocketLoadBalanceConcept.getInstance().onEstablish(connection);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().onClose(session.getId(), Connection.Type.OBSERVABLE, reason);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        WebSocketLoadBalanceConcept.getInstance().onMessage(session.getId(), Connection.Type.OBSERVABLE, message);
    }

    @OnMessage
    public void onMessage(Session session, PongMessage message) {
        WebSocketLoadBalanceConcept.getInstance().onMessage(session.getId(), Connection.Type.OBSERVABLE, message);
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer message) {
        WebSocketLoadBalanceConcept.getInstance().onMessage(session.getId(), Connection.Type.OBSERVABLE, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().onError(session.getId(), Connection.Type.OBSERVABLE, e);
    }
}
