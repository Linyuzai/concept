package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class ReactiveSubscribeWebSocketHandler implements WebSocketHandler {

    private WebSocketLoadBalanceConcept concept;

    private ConnectionServer connectionServer;

    private Consumer<Connection> connectionConsumer;

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        //session.getHandshakeInfo().getUri();
        Mono<Void> send = session.send(Flux.create(sink -> {
            Map<Object, Object> metadata = new LinkedHashMap<>();
            metadata.put(ConnectionServer.class, connectionServer);
            Connection open = concept.open(session, metadata, Connection.Type.SUBSCRIBER);
            connectionConsumer.accept(open);
        }));

        Mono<Void> receive = session.receive().map(it -> it.getPayload().asByteBuffer().array())
                .doOnNext(it -> concept.message(session.getId(), it, Connection.Type.SUBSCRIBER))
                .doOnError(it -> concept.error(session.getId(), it, Connection.Type.SUBSCRIBER))
                .then();

        return Mono.zip(send, receive).then();
    }
}
