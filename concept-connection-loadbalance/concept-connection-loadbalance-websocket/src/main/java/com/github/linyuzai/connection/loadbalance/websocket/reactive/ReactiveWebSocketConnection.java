package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.websocket.exception.WebSocketLoadBalanceException;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ReactiveWebSocketConnection extends AbstractConnection implements BiConsumer<WebSocketSession, FluxSink<WebSocketMessage>> {

    private volatile WebSocketSession session;

    private volatile FluxSink<WebSocketMessage> sender;

    private boolean needClose;

    private final List<Message> lazyMessages = Collections.synchronizedList(new ArrayList<>());


    public ReactiveWebSocketConnection(String type) {
        super(type);
    }

    public ReactiveWebSocketConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void send(Message message) {
        if (sender == null) {
            synchronized (this) {
                if (sender == null) {
                    lazyMessages.add(message);
                } else {
                    super.send(message);
                }
            }
        } else {
            super.send(message);
        }
    }

    @Override
    public void doSend(byte[] bytes) {
        //session.send(Flux.just(createMessage(bytes))).subscribe();
        sender.next(createMessage(bytes));
    }

    public WebSocketMessage createMessage(byte[] bytes) {
        return new WebSocketMessage(WebSocketMessage.Type.BINARY,
                session.bufferFactory().wrap(bytes));
    }

    @Override
    public Object getId() {
        checkConnectionOpen();
        return session.getId();
    }

    @Override
    public void close() {
        if (session == null) {
            synchronized (this) {
                if (session == null) {
                    needClose = true;
                } else {
                    close0();
                }
            }
        } else {
            close0();
        }
    }

    public void close0() {
        sender.complete();
        session.close().subscribe();
    }

    public void checkConnectionOpen() {
        if (session == null) {
            throw new WebSocketLoadBalanceException("Connection is not open");
        }
    }

    @Override
    public void accept(WebSocketSession session, FluxSink<WebSocketMessage> sender) {
        synchronized (this) {
            this.session = session;
            this.sender = sender;
            if (needClose) {
                close0();
            }
            for (Message message : lazyMessages) {
                super.send(message);
            }
            lazyMessages.clear();
        }
    }
}
