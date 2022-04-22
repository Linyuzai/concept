package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.*;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.Map;

@Getter
public class JavaxWebSocketConnection extends AbstractConnection {

    private final Session session;

    public JavaxWebSocketConnection(Session session) {
        this.session = session;
    }

    public JavaxWebSocketConnection(Session session,
                                    Map<Object, Object> metadata) {
        super(metadata);
        this.session = session;
    }

    @Override
    public Object getId() {
        return session.getId();
    }

    @Override
    public void doSend(byte[] bytes) {
        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
    }

    @SneakyThrows
    @Override
    public void close() {
        session.close();
    }
}
