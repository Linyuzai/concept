package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
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
            MessageCodecAdapter adapter = concept.getMessageCodecAdapter();
            connection.setMessageEncoder(adapter.getMessageEncoder(Connection.Type.OBSERVABLE));
            connection.setMessageDecoder(adapter.getMessageDecoder(Connection.Type.OBSERVABLE));
            connection.setConcept(concept);
            concept.open(connection);
        }));

        Mono<Void> receive = session.receive().map(WebSocketMessage::getPayload)
                .doOnNext(it -> concept.message(session.getId(), Connection.Type.OBSERVABLE, it))
                .doOnError(it -> concept.error(session.getId(), Connection.Type.OBSERVABLE, it))
                .then();

        return Mono.zip(send, receive).then();
    }


}
