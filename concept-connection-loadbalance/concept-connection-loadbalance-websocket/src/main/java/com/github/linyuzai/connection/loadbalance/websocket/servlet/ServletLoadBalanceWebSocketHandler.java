package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.socket.*;

@AllArgsConstructor
public class ServletLoadBalanceWebSocketHandler implements WebSocketHandler {

    protected final WebSocketLoadBalanceConcept concept;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        concept.open(session, null, Connection.Type.CLIENT);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        if (message instanceof PingMessage) {
            concept.message(session.getId(), ((PingMessage) message).getPayload().array(), Connection.Type.CLIENT);
        } else if (message instanceof PongMessage) {
            concept.message(session.getId(), ((PongMessage) message).getPayload().array(), Connection.Type.CLIENT);
        } else if (message instanceof BinaryMessage) {
            concept.message(session.getId(), ((BinaryMessage) message).getPayload().array(), Connection.Type.CLIENT);
        } else if (message instanceof TextMessage) {
            concept.message(session.getId(), ((TextMessage) message).asBytes(), Connection.Type.CLIENT);
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        concept.error(session.getId(), exception, Connection.Type.CLIENT);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        concept.close(session.getId(), Connection.Type.CLIENT);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
