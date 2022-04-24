package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageEncoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT + "{subtype}")
public class JavaxWebSocketLoadBalanceEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        JavaxWebSocketConnection connection =
                new JavaxWebSocketConnection(session, Connection.Type.OBSERVABLE);
        connection.getMetadata().put(Connection.URI, session.getRequestURI().toString());
        connection.setMessageEncoder(new JacksonSubscribeMessageEncoder());
        connection.setMessageDecoder(new JacksonSubscribeMessageDecoder());
        WebSocketLoadBalanceConcept.getInstance().open(connection);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId(), Connection.Type.OBSERVABLE, reason);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), Connection.Type.OBSERVABLE, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), Connection.Type.OBSERVABLE, e);
    }
}
