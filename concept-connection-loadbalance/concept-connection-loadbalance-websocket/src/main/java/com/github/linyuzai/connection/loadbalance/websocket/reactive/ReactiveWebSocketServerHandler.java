package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
public class ReactiveWebSocketServerHandler implements WebSocketHandler {

    private WebSocketLoadBalanceConcept concept;

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> send = session.send(Flux.create(sink -> {
            Map<Object, Object> metadata = new LinkedHashMap<>();
            metadata.put(Connection.URI, session.getHandshakeInfo().getUri().toString());
            concept.open(new Object[]{session, sink}, metadata);
        }));

        Mono<Void> receive = session.receive().map(it -> it.getPayload().asByteBuffer().array())
                .doOnNext(it -> concept.message(session.getId(), Connection.Type.CLIENT, it))
                .doOnError(it -> concept.error(session.getId(), Connection.Type.CLIENT, it))
                .then();

        return Mono.zip(send, receive).then();
    }
}
