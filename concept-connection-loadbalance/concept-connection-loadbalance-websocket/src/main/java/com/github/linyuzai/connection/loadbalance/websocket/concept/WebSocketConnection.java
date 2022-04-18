package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.*;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.Map;

@Getter
public class WebSocketConnection extends AbstractConnection {

    private final Session session;

    public WebSocketConnection(Session session, Map<String, String> metadata) {
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
