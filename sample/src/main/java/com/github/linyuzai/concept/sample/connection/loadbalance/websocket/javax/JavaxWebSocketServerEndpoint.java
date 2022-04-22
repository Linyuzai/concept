package com.github.linyuzai.concept.sample.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
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
        Map<Object, Object> metadata = new LinkedHashMap<>();
        metadata.put("type", type);
        metadata.put("token", token);
        WebSocketLoadBalanceConcept.getInstance().open(session, metadata, Connection.Type.CLIENT);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId(), Connection.Type.CLIENT);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), message, Connection.Type.CLIENT);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), e, Connection.Type.CLIENT);
    }
}
