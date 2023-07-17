package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 基于 {@link ReactiveWebSocketConnection} 的服务间负载均衡的 {@link WebSocketHandler}。
 * <p>
 * {@link WebSocketHandler} based on {@link ReactiveWebSocketConnection} for service load balancing.
 */
@AllArgsConstructor
public class ReactiveWebSocketLoadBalanceHandler implements WebSocketHandler {

    private WebSocketLoadBalanceConcept concept;

    @NonNull
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> send = session.send(Flux.<WebSocketMessage>create(sink -> {
            ReactiveWebSocketConnection connection =
                    new ReactiveWebSocketConnection(session, sink);
            connection.setType(Connection.Type.OBSERVABLE);
            concept.onEstablish(connection);
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
