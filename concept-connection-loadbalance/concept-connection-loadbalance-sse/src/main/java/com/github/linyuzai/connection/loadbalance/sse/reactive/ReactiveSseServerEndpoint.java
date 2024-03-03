package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;

@RestController
@RequestMapping("${concept.sse.server.default-endpoint.prefix:concept-sse}")
@RequiredArgsConstructor
public class ReactiveSseServerEndpoint {

    private final SseIdGenerator sseIdGenerator;

    private final SseFluxFactory sseFluxFactory;

    private final SseLoadBalanceConcept concept;

    @GetMapping("{path}")
    public Flux<ServerSentEvent<Object>> defaultEndpoint(@PathVariable String path, @RequestParam Map<Object, Object> params) {
        Object id = sseIdGenerator.generateId(params);
        return sseFluxFactory.create(Flux.create((FluxSink<ServerSentEvent<Object>> fluxSink) -> {
                    ReactiveSseCreateRequest request = new ReactiveSseCreateRequest(id, path, fluxSink);
                    concept.onEstablish(request, params);
                }))
                .doOnError(it -> concept.onError(id, Connection.Type.CLIENT, it))
                .doOnComplete(() -> concept.onClose(id, Connection.Type.CLIENT, null));
    }
}
