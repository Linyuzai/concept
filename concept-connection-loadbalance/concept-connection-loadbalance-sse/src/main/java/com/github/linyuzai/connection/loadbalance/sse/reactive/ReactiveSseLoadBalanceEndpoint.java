package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@RestController
@RequestMapping(SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT)
@RequiredArgsConstructor
public class ReactiveSseLoadBalanceEndpoint {

    private final SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private final SseFluxFactory sseFluxFactory = new DefaultSseFluxFactory();

    private final SseLoadBalanceConcept concept;

    @GetMapping("{path}")
    public Flux<ServerSentEvent<Object>> defaultEndpoint(@PathVariable String path) {
        Object id = sseIdGenerator.generateId(null);
        return sseFluxFactory.create(Flux.create((FluxSink<ServerSentEvent<Object>> fluxSink) -> {
                    ReactiveSseCreateRequest request = new ReactiveSseCreateRequest(id, path, fluxSink);
                    ReactiveSseConnection connection = new ReactiveSseConnection(fluxSink);
                    connection.setCreateRequest(request);
                    connection.setType(Connection.Type.OBSERVABLE);
                    concept.onEstablish(connection);
                }))
                .doOnError(it -> concept.onError(id, Connection.Type.OBSERVABLE, it))
                .doOnComplete(() -> concept.onClose(id, Connection.Type.OBSERVABLE, null));
    }
}
