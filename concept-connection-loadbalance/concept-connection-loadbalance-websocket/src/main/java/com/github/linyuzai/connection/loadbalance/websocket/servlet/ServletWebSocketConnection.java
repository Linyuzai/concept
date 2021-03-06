package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.SneakyThrows;
import org.springframework.web.socket.*;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 基于 {@link WebSocketSession} 的 {@link WebSocketConnection} 实现
 */
public class ServletWebSocketConnection extends WebSocketConnection {

    private final WebSocketSession session;

    public ServletWebSocketConnection(WebSocketSession session, String type) {
        super(type);
        this.session = session;
    }

    public ServletWebSocketConnection(WebSocketSession session, String type, Map<Object, Object> metadata) {
        super(type, metadata);
        this.session = session;
    }

    @Override
    public Object getId() {
        return session.getId();
    }

    @Override
    public URI getUri() {
        return session.getUri();
    }

    @SneakyThrows
    @Override
    public void doSend(Object message) {
        if (message instanceof WebSocketMessage) {
            session.sendMessage((WebSocketMessage<?>) message);
        } else if (message instanceof String) {
            session.sendMessage(new TextMessage((CharSequence) message));
        } else if (message instanceof ByteBuffer) {
            session.sendMessage(new BinaryMessage((ByteBuffer) message));
        } else if (message instanceof byte[]) {
            session.sendMessage(new BinaryMessage(ByteBuffer.wrap((byte[]) message)));
        } else {
            throw new IllegalArgumentException(message.toString());
        }
    }

    @SneakyThrows
    @Override
    public void ping(PingMessage ping) {
        session.sendMessage(new org.springframework.web.socket.PingMessage(ping.getPayload()));
    }

    @SneakyThrows
    @Override
    public void pong(PongMessage pong) {
        session.sendMessage(new org.springframework.web.socket.PongMessage(pong.getPayload()));
    }

    @SneakyThrows
    @Override
    public void doClose(Object reason) {
        session.close((CloseStatus) reason);
    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return new CloseStatus(code, reason);
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }
}
