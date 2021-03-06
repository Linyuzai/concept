package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

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
    public void doSend(Object message) {
        if (message instanceof String) {
            session.getAsyncRemote().sendText((String) message);
        } else if (message instanceof ByteBuffer) {
            session.getAsyncRemote().sendBinary((ByteBuffer) message);
        } else if (message instanceof byte[]) {
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap((byte[]) message));
        } else {
            session.getAsyncRemote().sendObject(message);
        }
    }

    @SneakyThrows
    @Override
    public void ping(PingMessage ping) {
        session.getAsyncRemote().sendPing(ping.getPayload());
    }

    @SneakyThrows
    @Override
    public void pong(PongMessage pong) {
        session.getAsyncRemote().sendPong(pong.getPayload());
    }

    @SneakyThrows
    @Override
    public void doClose(Object reason) {
        session.close((CloseReason) reason);
    }

    @Override
    public CloseReason getCloseReason(int code, String reason) {
        return new CloseReason(CloseReason.CloseCodes.getCloseCode(code), reason);
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
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
