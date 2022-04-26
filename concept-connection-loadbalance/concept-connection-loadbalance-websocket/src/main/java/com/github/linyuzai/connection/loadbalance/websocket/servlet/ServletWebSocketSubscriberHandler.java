package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.*;

@RequiredArgsConstructor
public class ServletWebSocketSubscriberHandler implements WebSocketHandler {

    @Getter
    private WebSocketSession session;

    private final WebSocketLoadBalanceConcept concept;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        this.session = session;
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        concept.message(session.getId(), Connection.Type.SUBSCRIBER, message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        concept.error(session.getId(), Connection.Type.SUBSCRIBER, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        concept.close(session.getId(), Connection.Type.SUBSCRIBER, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
