package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.SneakyThrows;
import org.springframework.web.socket.*;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

public class ServletWebSocketConnection extends WebSocketConnection {

    private final WebSocketSession session;

    public ServletWebSocketConnection(WebSocketSession session, String type) {
        super(type);
        this.session = session;
        configure();
    }

    public ServletWebSocketConnection(WebSocketSession session, String type, Map<Object, Object> metadata) {
        super(type, metadata);
        this.session = session;
        configure();
    }

    protected void configure() {
        if (!getMetadata().containsKey(Connection.URI)) {
            getMetadata().put(Connection.URI, Objects.requireNonNull(session.getUri()).getPath());
        }
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
