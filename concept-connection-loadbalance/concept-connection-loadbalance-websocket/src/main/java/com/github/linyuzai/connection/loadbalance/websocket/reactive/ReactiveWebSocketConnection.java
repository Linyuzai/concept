package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
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
import java.util.function.Consumer;

/**
 * 基于 {@link WebSocketSession} 的 {@link WebSocketConnection} 实现
 */
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
    public void doSend(Object message, Runnable success, Consumer<Throwable> error) {
        //session.send(Flux.just(createMessage(bytes))).subscribe();
        try {
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
            success.run();
        } catch (Throwable e) {
            error.accept(new MessageTransportException(e));
        }
    }

    @Override
    public void doPing(PingMessage message, Runnable success, Consumer<Throwable> error) {
        sender.next(session.pingMessage(factory -> factory.wrap(message.getPayload())));
    }

    @Override
    public void doPong(PongMessage message, Runnable success, Consumer<Throwable> error) {
        sender.next(session.pongMessage(factory -> factory.wrap(message.getPayload())));
    }

    @Override
    public void doClose(Object reason) {
        sender.complete();
        session.close((CloseStatus) reason).subscribe();
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
