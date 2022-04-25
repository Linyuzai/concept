package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.Map;

@Getter
public class JavaxWebSocketConnection extends AbstractConnection {

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
    public void doSend(Object message) {
        if (message instanceof String) {
            session.getAsyncRemote().sendText((String) message);
        } else if (message instanceof byte[]) {
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap((byte[]) message));
        } else if (message instanceof ByteBuffer) {
            session.getAsyncRemote().sendBinary((ByteBuffer) message);
        } else {
            session.getAsyncRemote().sendObject(message);
        }
    }

    @SneakyThrows
    @Override
    public void ping(Object ping) {
        if (ping instanceof byte[]) {
            session.getAsyncRemote().sendPing(ByteBuffer.wrap((byte[]) ping));
        } else if (ping instanceof ByteBuffer) {
            session.getAsyncRemote().sendPing((ByteBuffer) ping);
        }
    }

    @SneakyThrows
    @Override
    public void pong(Object pong) {
        if (pong instanceof byte[]) {
            session.getAsyncRemote().sendPong(ByteBuffer.wrap((byte[]) pong));
        } else if (pong instanceof ByteBuffer) {
            session.getAsyncRemote().sendPong((ByteBuffer) pong);
        }
    }

    @SneakyThrows
    @Override
    public void close() {
        session.close();
    }
}
