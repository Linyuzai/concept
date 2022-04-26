package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.socket.*;

@AllArgsConstructor
public class ServletWebSocketServerHandler implements WebSocketHandler {

    protected final WebSocketLoadBalanceConcept concept;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        concept.open(session, null);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        concept.message(session.getId(), Connection.Type.CLIENT, message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        concept.error(session.getId(), Connection.Type.CLIENT, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        concept.close(session.getId(), Connection.Type.CLIENT, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
