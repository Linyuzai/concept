package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 基于 {@link ServletWebSocketConnection} 默认服务的 {@link WebSocketHandler}。
 * <p>
 * {@link WebSocketHandler} for default service based on {@link ServletWebSocketConnection}.
 */
@Getter
@RequiredArgsConstructor
public class ServletWebSocketServerHandler implements WebSocketHandler {

    protected final WebSocketLoadBalanceConcept concept;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        concept.onEstablish(session, null);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        concept.onMessage(session.getId(), Connection.Type.CLIENT, message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        concept.onError(session.getId(), Connection.Type.CLIENT, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        concept.onClose(session.getId(), Connection.Type.CLIENT, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
