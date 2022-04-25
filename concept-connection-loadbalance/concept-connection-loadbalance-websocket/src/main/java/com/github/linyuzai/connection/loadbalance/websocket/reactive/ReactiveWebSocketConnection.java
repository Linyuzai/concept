package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ReactiveWebSocketConnection extends AbstractConnection {

    private final WebSocketSession session;

    private final FluxSink<WebSocketMessage> sender;

    public ReactiveWebSocketConnection(WebSocketSession session, FluxSink<WebSocketMessage> sender, String type) {
        super(type);
        this.session = session;
        this.sender = sender;
    }

    public ReactiveWebSocketConnection(WebSocketSession session, FluxSink<WebSocketMessage> sender, String type, Map<Object, Object> metadata) {
        super(type, metadata);
        this.session = session;
        this.sender = sender;
    }

    @Override
    public Object getId() {
        return session.getId();
    }

    @Override
    public void doSend(Object message) {
        //session.send(Flux.just(createMessage(bytes))).subscribe();
        if (message instanceof String) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.TEXT,
                    session.bufferFactory().wrap(((String) message)
                            .getBytes(StandardCharsets.UTF_8))));
        } else if (message instanceof DataBuffer) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.BINARY,
                    (DataBuffer) message));
        } else if (message instanceof byte[]) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.BINARY,
                    session.bufferFactory().wrap((byte[]) message)));
        } else if (message instanceof ByteBuffer) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.BINARY,
                    session.bufferFactory().wrap((ByteBuffer) message)));
        }
    }

    @Override
    public boolean payloadSupportPingOrPong(Object payload) {
        return payload instanceof byte[] || payload instanceof ByteBuffer || payload instanceof DataBuffer;
    }

    @Override
    public void ping(Object ping) {
        if (ping instanceof byte[]) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.PING,
                    session.bufferFactory().wrap((byte[]) ping)));
        } else if (ping instanceof ByteBuffer) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.PING,
                    session.bufferFactory().wrap((ByteBuffer) ping)));
        } else if (ping instanceof DataBuffer) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.PING,
                    (DataBuffer) ping));
        }
    }

    @Override
    public void pong(Object pong) {
        if (pong instanceof byte[]) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.PONG,
                    session.bufferFactory().wrap((byte[]) pong)));
        } else if (pong instanceof ByteBuffer) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.PONG,
                    session.bufferFactory().wrap((ByteBuffer) pong)));
        } else if (pong instanceof DataBuffer) {
            sender.next(new WebSocketMessage(WebSocketMessage.Type.PONG,
                    (DataBuffer) pong));
        }
    }

    @Override
    public void close() {
        sender.complete();
        session.close().subscribe();
    }
}
