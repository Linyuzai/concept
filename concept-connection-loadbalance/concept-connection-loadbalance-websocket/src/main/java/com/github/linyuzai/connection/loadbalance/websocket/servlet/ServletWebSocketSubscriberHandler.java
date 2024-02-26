package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Consumer;

/**
 * 基于 {@link ServletWebSocketConnection} 转发消息客户端的 {@link WebSocketHandler}。
 * <p>
 * {@link WebSocketHandler} for forwarding message client based on {@link ServletWebSocketConnection}.
 */
@Getter
@RequiredArgsConstructor
public class ServletWebSocketSubscriberHandler implements WebSocketHandler {

    private final ConnectionLoadBalanceConcept concept;

    private final Consumer<WebSocketSession> consumer;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        consumer.accept(session);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        concept.onMessage(session.getId(), Connection.Type.SUBSCRIBER, message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        concept.onError(session.getId(), Connection.Type.SUBSCRIBER, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        concept.onClose(session.getId(), Connection.Type.SUBSCRIBER, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
