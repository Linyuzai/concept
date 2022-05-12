package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.socket.*;

import java.util.function.Consumer;

@AllArgsConstructor
public class ServletWebSocketSubscriberHandler implements WebSocketHandler {

    private final WebSocketLoadBalanceConcept concept;

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
