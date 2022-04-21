package com.github.linyuzai.concept.sample.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint("/concept-ws/{type}/{token}")
public class JavaxWebSocketServerEndpoint {

    @OnOpen
    public void onOpen(Session session,
                       EndpointConfig config,
                       @PathParam(value = "type") String type,
                       @PathParam(value = "token") String token) {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("type", type);
        metadata.put("token", token);
        WebSocketLoadBalanceConcept.getInstance().open(session, metadata);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().close(session);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session, e);
    }
}
