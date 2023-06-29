package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.Getter;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基于 {@link Session} 的 {@link WebSocketConnection} 实现
 */
@Getter
public class JavaxWebSocketConnection extends WebSocketConnection {

    private final Session session;

    public JavaxWebSocketConnection(Session session, String type) {
        super(type);
        this.session = session;
    }

    public JavaxWebSocketConnection(Session session,
                                    String type,
                                    Map<Object, Object> metadata) {
        super(type, metadata);
        this.session = session;
    }

    @Override
    public Object getId() {
        return session.getId();
    }

    @Override
    public URI getUri() {
        return session.getRequestURI();
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (message instanceof String) {
                session.getBasicRemote().sendText((String) message);
            } else if (message instanceof ByteBuffer) {
                session.getBasicRemote().sendBinary((ByteBuffer) message);
            } else if (message instanceof byte[]) {
                session.getBasicRemote().sendBinary(ByteBuffer.wrap((byte[]) message));
            } else {
                session.getBasicRemote().sendObject(message);
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
            session.getBasicRemote().sendPing(message.getPayload());
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            session.getBasicRemote().sendPong(message.getPayload());
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            session.close((CloseReason) reason);
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public CloseReason getCloseReason(int code, String reason) {
        return new CloseReason(CloseReason.CloseCodes.getCloseCode(code), reason);
    }

    @Override
    protected void parseQueryParameterMap(Map<String, String> map) {
        for (Map.Entry<String, List<String>> entry : session.getRequestParameterMap().entrySet()) {
            List<String> value = entry.getValue();
            if (!value.isEmpty()) {
                map.put(entry.getKey(), value.get(0));
            }
        }
    }
}
