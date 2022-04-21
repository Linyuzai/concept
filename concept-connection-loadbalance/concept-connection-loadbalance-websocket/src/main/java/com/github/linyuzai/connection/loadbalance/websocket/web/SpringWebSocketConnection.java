package com.github.linyuzai.connection.loadbalance.websocket.web;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.SneakyThrows;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class SpringWebSocketConnection extends AbstractConnection {

    private final WebSocketSession session;

    public SpringWebSocketConnection(WebSocketSession session) {
        this.session = session;
    }

    public SpringWebSocketConnection(Map<String, String> metadata, WebSocketSession session) {
        super(metadata);
        this.session = session;
    }

    @SneakyThrows
    @Override
    public void doSend(byte[] bytes) {
        session.sendMessage(new BinaryMessage(bytes));
    }

    @Override
    public Object getId() {
        return session.getId();
    }

    @SneakyThrows
    @Override
    public void close() {
        session.close();
    }
}
