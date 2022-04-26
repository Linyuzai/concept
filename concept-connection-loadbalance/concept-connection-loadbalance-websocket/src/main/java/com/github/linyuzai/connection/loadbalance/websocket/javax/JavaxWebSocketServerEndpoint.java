package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX + "/{type}")
public class JavaxWebSocketServerEndpoint {

    @OnOpen
    public void onOpen(Session session,
                       EndpointConfig config,
                       @PathParam(value = "type") String type) {
        Map<Object, Object> metadata = new LinkedHashMap<>();
        metadata.put("type", type);
        WebSocketLoadBalanceConcept.getInstance().open(session, metadata);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().close(session.getId(), Connection.Type.CLIENT, reason);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), Connection.Type.CLIENT, message);
    }

    @OnMessage
    public void onMessage(Session session, PongMessage message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), Connection.Type.CLIENT, message);
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer message) {
        WebSocketLoadBalanceConcept.getInstance().message(session.getId(), Connection.Type.CLIENT, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().error(session.getId(), Connection.Type.CLIENT, e);
    }
}
