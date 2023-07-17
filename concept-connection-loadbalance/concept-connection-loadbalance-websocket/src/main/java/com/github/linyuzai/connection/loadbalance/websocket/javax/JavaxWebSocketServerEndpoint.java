package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;

/**
 * 基于 {@link JavaxWebSocketConnection} 默认服务的端点配置。
 * <p>
 * Endpoint configuration based on {@link JavaxWebSocketConnection} default service.
 */
@ServerEndpoint(WebSocketLoadBalanceConcept.SERVER_ENDPOINT_PREFIX + "{type}")
public class JavaxWebSocketServerEndpoint {

    @OnOpen
    public void onOpen(Session session,
                       EndpointConfig config,
                       @PathParam(value = "type") String type) {
        WebSocketLoadBalanceConcept.getInstance().onEstablish(session, null);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WebSocketLoadBalanceConcept.getInstance().onClose(session.getId(), Connection.Type.CLIENT, reason);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        WebSocketLoadBalanceConcept.getInstance().onMessage(session.getId(), Connection.Type.CLIENT, message);
    }

    @OnMessage
    public void onMessage(Session session, PongMessage message) {
        WebSocketLoadBalanceConcept.getInstance().onMessage(session.getId(), Connection.Type.CLIENT, message);
    }

    @OnMessage
    public void onMessage(Session session, ByteBuffer message) {
        WebSocketLoadBalanceConcept.getInstance().onMessage(session.getId(), Connection.Type.CLIENT, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        WebSocketLoadBalanceConcept.getInstance().onError(session.getId(), Connection.Type.CLIENT, e);
    }
}
