package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyMarker;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(WebSocketLoadBalanceConcept.ENDPOINT_PREFIX + "{type}")
public class JavaxWebSocketProxyEndpoint {

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "type") String type) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(ProxyMarker.FLAG, type);
        WebSocketLoadBalanceConcept.getInstance().open(session, metadata);
    }

    @OnClose
    public void onClose(Session session) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId());
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), e);
    }
}
