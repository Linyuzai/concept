package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ReactiveWebSocketLoadBalanceHandler implements WebSocketHandler {

    private WebSocketLoadBalanceConcept concept;

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> send = session.send(Flux.create(sink -> {
            ReactiveWebSocketConnection connection =
                    new ReactiveWebSocketConnection(session, sink, Connection.Type.OBSERVABLE);
            concept.onOpen(connection);
        }));

        Mono<Void> receive = session.receive()
                .doOnNext(it -> concept.onMessage(session.getId(), Connection.Type.OBSERVABLE, it))
                .doOnError(it -> concept.onError(session.getId(), Connection.Type.OBSERVABLE, it))
                .then();

        @SuppressWarnings("all")
        Disposable disposable = session.closeStatus()
                .doOnError(it -> concept.onError(session.getId(), Connection.Type.OBSERVABLE, it))
                .subscribe(it -> concept.onClose(session.getId(), Connection.Type.OBSERVABLE, it));

        return Mono.zip(send, receive).then();
    }
}
