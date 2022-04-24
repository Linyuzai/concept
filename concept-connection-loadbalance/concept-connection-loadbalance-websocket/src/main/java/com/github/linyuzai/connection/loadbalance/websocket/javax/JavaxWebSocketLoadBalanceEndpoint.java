package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageEncoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT_PREFIX + "{subtype}")
public class JavaxWebSocketLoadBalanceEndpoint {

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "subtype") String subtype) {
        Map<Object, Object> metadata = new LinkedHashMap<>();
        metadata.put(Connection.URI, session.getRequestURI().toString());
        JavaxWebSocketConnection connection =
                new JavaxWebSocketConnection(session, Connection.Type.OBSERVABLE, metadata);
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
