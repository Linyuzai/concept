package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReactiveWebSocketConnection extends AbstractConnection {

    private final WebSocketSession session;

    private final FluxSink<WebSocketMessage> sender;

    private boolean needClose;

    private final List<Message> lazyMessages = Collections.synchronizedList(new ArrayList<>());


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
        return session.getId();
    }

    @Override
    public void close() {
        sender.complete();
        session.close().subscribe();
    }
}
