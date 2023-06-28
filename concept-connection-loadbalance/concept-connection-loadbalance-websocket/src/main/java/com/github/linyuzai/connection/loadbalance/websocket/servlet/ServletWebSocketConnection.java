package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.SneakyThrows;
import org.springframework.web.socket.*;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.function.Consumer;

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

    @Override
    public void doSend(Object message, Runnable success, Consumer<Throwable> error) {
        try {
            if (message instanceof WebSocketMessage) {
                session.sendMessage((WebSocketMessage<?>) message);
                success.run();
                return;
            } else if (message instanceof String) {
                session.sendMessage(new TextMessage((CharSequence) message));
                success.run();
                return;
            } else if (message instanceof ByteBuffer) {
                session.sendMessage(new BinaryMessage((ByteBuffer) message));
                success.run();
                return;
            } else if (message instanceof byte[]) {
                session.sendMessage(new BinaryMessage(ByteBuffer.wrap((byte[]) message)));
                success.run();
                return;
            }
        } catch (Throwable e) {
            error.accept(new MessageTransportException(e));
            return;
        }
        throw new IllegalArgumentException(message.toString());
    }

    @Override
    public void doPing(PingMessage message, Runnable success, Consumer<Throwable> error) {
        try {
            session.sendMessage(new org.springframework.web.socket.PingMessage(message.getPayload()));
            success.run();
        } catch (Throwable e) {
            error.accept(new MessageTransportException(e));
        }
    }

    @Override
    public void doPong(PongMessage message, Runnable success, Consumer<Throwable> error) {
        try {
            session.sendMessage(new org.springframework.web.socket.PongMessage(message.getPayload()));
            success.run();
        } catch (Throwable e) {
            error.accept(new MessageTransportException(e));
        }
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
