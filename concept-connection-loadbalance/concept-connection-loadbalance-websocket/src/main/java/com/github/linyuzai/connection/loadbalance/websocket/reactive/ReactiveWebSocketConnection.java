package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * 基于 {@link WebSocketSession} 的 {@link WebSocketConnection} 实现。
 * <p>
 * {@link WebSocketConnection} implementation based on {@link WebSocketSession}.
 */
@Getter
@RequiredArgsConstructor
public class ReactiveWebSocketConnection extends WebSocketConnection {

    private final WebSocketSession session;

    private final FluxSink<WebSocketMessage> sender;

    @Override
    public Object getId() {
        return session.getId();
    }

    @Override
    public URI getUri() {
        return session.getHandshakeInfo().getUri();
    }

    @Override
    public boolean isOpen() {
        return session.isOpen();
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
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
                sender.next(session.textMessage(message.toString()));
            }
            onSuccess.run();
        } catch (WebClientException e) {
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
            sender.next(session.pingMessage(factory -> factory.wrap(message.getPayload())));
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
            sender.next(session.pongMessage(factory -> factory.wrap(message.getPayload())));
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        sender.complete();
        session.close((CloseStatus) reason)
                .doOnSuccess(v -> onSuccess.run())
                .doOnError(onError)
                .doOnTerminate(onComplete)
                .subscribe();
    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return new CloseStatus(code, reason);
    }
}
