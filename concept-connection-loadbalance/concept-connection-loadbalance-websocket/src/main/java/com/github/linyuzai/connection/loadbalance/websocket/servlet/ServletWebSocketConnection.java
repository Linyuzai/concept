package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.SneakyThrows;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

public class ServletWebSocketConnection extends AbstractConnection {

    private final WebSocketSession session;

    public ServletWebSocketConnection(WebSocketSession session, String type) {
        super(type);
        this.session = session;
    }

    public ServletWebSocketConnection(WebSocketSession session, String type, Map<Object, Object> metadata) {
        super(type, metadata);
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
