package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;

@Getter
@AllArgsConstructor
public class ReactiveWebSocketSubscriberHandler implements WebSocketHandler {

    private WebSocketLoadBalanceConcept concept;

    private BiConsumer<WebSocketSession, FluxSink<WebSocketMessage>> sessionConsumer;

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> send = session.send(Flux.create(sink -> sessionConsumer.accept(session, sink)))
                .doOnError(it -> concept.error(session.getId(), Connection.Type.SUBSCRIBER, it));

        Mono<Void> receive = session.receive()
                .doOnNext(it -> concept.message(session.getId(), Connection.Type.SUBSCRIBER, it))
                .doOnError(it -> concept.error(session.getId(), Connection.Type.SUBSCRIBER, it))
                .then();

        return Mono.zip(send, receive).then();
    }
}
