package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.Map;

public class ReactiveWebSocketConnection extends AbstractConnection {

    private final WebSocketSession session;

    //private FluxSink<WebSocketMessage> sender;

    public ReactiveWebSocketConnection(WebSocketSession session) {
        this.session = session;
    }

    public ReactiveWebSocketConnection(Map<String, String> metadata, WebSocketSession session) {
        super(metadata);
        this.session = session;
    }

    @Override
    public void doSend(byte[] bytes) {
        //sender.next(new WebSocketMessage(WebSocketMessage.Type.BINARY, session.bufferFactory().wrap(bytes)));
    }

    @Override
    public Object getId() {
        return session.getId();
    }

    @Override
    public void close() {
        session.close().subscribe();
    }
}
