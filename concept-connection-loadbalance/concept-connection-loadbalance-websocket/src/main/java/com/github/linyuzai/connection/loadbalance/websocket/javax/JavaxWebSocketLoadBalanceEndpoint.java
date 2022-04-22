package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ProxyMarker;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(WebSocketLoadBalanceConcept.ENDPOINT_PREFIX + "{type}")
public class JavaxWebSocketLoadBalanceEndpoint {

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "type") String type) {
        Map<Object, Object> metadata = new LinkedHashMap<>();
        metadata.put(ProxyMarker.FLAG, type);
        WebSocketLoadBalanceConcept.getInstance().open(session, metadata, Connection.Type.OBSERVABLE);
    }

    @OnClose
    public void onClose(Session session) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId(), Connection.Type.OBSERVABLE);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), message, Connection.Type.OBSERVABLE);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), e, Connection.Type.OBSERVABLE);
    }
}
