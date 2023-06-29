package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import org.springframework.web.socket.*;

import java.io.IOException;
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
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (message instanceof WebSocketMessage) {
                session.sendMessage((WebSocketMessage<?>) message);
            } else if (message instanceof String) {
                session.sendMessage(new TextMessage((CharSequence) message));
            } else if (message instanceof ByteBuffer) {
                session.sendMessage(new BinaryMessage((ByteBuffer) message));
            } else if (message instanceof byte[]) {
                session.sendMessage(new BinaryMessage(ByteBuffer.wrap((byte[]) message)));
            } else {
                session.sendMessage(new TextMessage(message.toString()));
            }
            onSuccess.run();
        } catch (IOException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            session.sendMessage(new org.springframework.web.socket.PingMessage(message.getPayload()));
            onSuccess.run();
        } catch (IOException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            session.sendMessage(new org.springframework.web.socket.PongMessage(message.getPayload()));
            onSuccess.run();
        } catch (IOException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            session.close((CloseStatus) reason);
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return new CloseStatus(code, reason);
    }
}
