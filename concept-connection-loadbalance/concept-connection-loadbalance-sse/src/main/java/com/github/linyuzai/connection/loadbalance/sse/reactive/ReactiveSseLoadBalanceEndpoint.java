package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Getter
@Setter
@RestController
@RequestMapping(SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT)
@RequiredArgsConstructor
public class ReactiveSseLoadBalanceEndpoint {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private SseFluxFactory sseFluxFactory = new DefaultSseFluxFactory();

    private final SseLoadBalanceConcept concept;

    @GetMapping
    public Flux<ServerSentEvent<Object>> loadBalanceEndpoint() {
        Object id = sseIdGenerator.generateId(null);
        return sseFluxFactory.create(Flux.create((FluxSink<ServerSentEvent<Object>> fluxSink) -> {
                    ReactiveSseCreateRequest request = new ReactiveSseCreateRequest(id, SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT, fluxSink);
                    ReactiveSseConnection connection = new ReactiveSseConnection(fluxSink);
                    connection.setCreateRequest(request);
                    connection.setType(Connection.Type.OBSERVABLE);
                    concept.onEstablish(connection);
                }))
                .doOnError(it -> concept.onError(id, Connection.Type.OBSERVABLE, it))
                .doOnComplete(() -> concept.onClose(id, Connection.Type.OBSERVABLE, null));
    }
}
