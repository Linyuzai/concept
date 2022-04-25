package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.SneakyThrows;
import org.springframework.web.socket.*;

import java.nio.ByteBuffer;
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

    @Override
    public Object getId() {
        return session.getId();
    }

    @SneakyThrows
    @Override
    public void doSend(Object message) {
        if (message instanceof String) {
            session.sendMessage(new TextMessage((CharSequence) message));
        } else if (message instanceof byte[]) {
            session.sendMessage(new BinaryMessage(ByteBuffer.wrap((byte[]) message)));
        } else if (message instanceof ByteBuffer) {
            session.sendMessage(new BinaryMessage((ByteBuffer) message));
        }
    }

    @SneakyThrows
    @Override
    public void ping(Object ping) {
        if (ping instanceof byte[]) {
            session.sendMessage(new PingMessage(ByteBuffer.wrap((byte[]) ping)));
        } else if (ping instanceof ByteBuffer) {
            session.sendMessage(new PingMessage((ByteBuffer) ping));
        }
    }

    @SneakyThrows
    @Override
    public void pong(Object pong) {
        if (pong instanceof byte[]) {
            session.sendMessage(new PongMessage(ByteBuffer.wrap((byte[]) pong)));
        } else if (pong instanceof ByteBuffer) {
            session.sendMessage(new PongMessage((ByteBuffer) pong));
        }
    }

    @SneakyThrows
    @Override
    public void close() {
        session.close();
    }
}
