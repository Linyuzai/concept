package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@AllArgsConstructor
public class ReactiveLoadBalanceWebSocketHandler implements WebSocketHandler {

    private WebSocketLoadBalanceConcept concept;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        /*Mono<Void> send = session.send(Flux.create(sink -> {
            Connection connection = concept.create(session, null);
        }));*/
        session.getHandshakeInfo().getUri();
        concept.add(session, null);

        Mono<Void> receive = session.receive().map(it -> it.getPayload().asByteBuffer().array())
                .doOnNext(it -> concept.message(session.getId(), it))
                .doOnError(it -> concept.error(session.getId(), it))
                .then();

        return receive;

        //return Mono.zip(send, receive).then();
    }
}
