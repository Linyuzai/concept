package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;

/**
 * 基于 {@link ReactiveWebSocketConnection} 转发消息客户端的 {@link WebSocketHandler}
 */
@Getter
@AllArgsConstructor
public class ReactiveWebSocketSubscriberHandler implements WebSocketHandler {

    private ConnectionLoadBalanceConcept concept;

    private BiConsumer<WebSocketSession, FluxSink<WebSocketMessage>> sessionConsumer;

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> send = session.send(Flux.<WebSocketMessage>create(sink -> sessionConsumer.accept(session, sink))
                .doOnError(it -> concept.onError(session.getId(), Connection.Type.SUBSCRIBER, it)));

        Mono<Void> receive = session.receive()
                .doOnNext(it -> concept.onMessage(session.getId(), Connection.Type.SUBSCRIBER, it))
                .doOnError(it -> concept.onError(session.getId(), Connection.Type.SUBSCRIBER, it))
                .then();

        @SuppressWarnings("all")
        Disposable disposable = session.closeStatus()
                .doOnError(it -> concept.onError(session.getId(), Connection.Type.SUBSCRIBER, it))
                .subscribe(it -> concept.onClose(session.getId(), Connection.Type.SUBSCRIBER, it));

        return Mono.zip(send, receive).then();
    }
}
