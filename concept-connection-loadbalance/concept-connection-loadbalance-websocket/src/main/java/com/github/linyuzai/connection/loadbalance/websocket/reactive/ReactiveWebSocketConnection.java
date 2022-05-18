package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

public class ReactiveWebSocketConnection extends WebSocketConnection {

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
    public URI getUri() {
        return session.getHandshakeInfo().getUri();
    }

    @Override
    public void doSend(Object message) {
        //session.send(Flux.just(createMessage(bytes))).subscribe();
        if (message instanceof WebSocketMessage) {
            sender.next((WebSocketMessage) message);
        } else if (message instanceof String) {
            sender.next(session.textMessage((String) message));
        } else if (message instanceof DataBuffer) {
            sender.next(session.binaryMessage(factory -> (DataBuffer) message));
        } else if (message instanceof ByteBuffer) {
            sender.next(session.binaryMessage(factory -> factory.wrap((ByteBuffer) message)));
        } else if (message instanceof byte[]) {
            sender.next(session.binaryMessage(factory -> factory.wrap((byte[]) message)));
        } else {
            throw new IllegalArgumentException(message.toString());
        }
    }

    @Override
    public void ping(PingMessage ping) {
        sender.next(session.pingMessage(factory -> factory.wrap(ping.getPayload())));
    }

    @Override
    public void pong(PongMessage pong) {
        sender.next(session.pongMessage(factory -> factory.wrap(pong.getPayload())));
    }

    @Override
    public void close(int code, String reason) {
        sender.complete();
        if (reason == null) {
            session.close().subscribe();
        } else {
            session.close(new CloseStatus(code, reason)).subscribe();
        }
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }
}
