package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.*;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${concept.sse.server.default-endpoint.prefix:concept-sse}")
@RequiredArgsConstructor
public class ReactiveSseServerEndpoint {

    private final SseIdGenerator sseIdGenerator;

    private final SseFluxFactory sseFluxFactory;

    private final SseLoadBalanceConcept concept;

    private final List<SseRequestInterceptor> interceptors;

    @GetMapping("{path}")
    public Publisher<?> defaultEndpoint(@PathVariable String path,
                                        @RequestParam Map<Object, Object> params,
                                        ServerHttpRequest request,
                                        ServerHttpResponse response) {
        SseRequest req = new ReactiveSseRequest(request);
        SseResponse resp = new ReactiveSseResponse(response);
        for (SseRequestInterceptor interceptor : interceptors) {
            if (interceptor.intercept(req, resp)) {
                return Mono.empty();
            }
        }
        Object id = sseIdGenerator.generateId(params);
        return sseFluxFactory.create(Flux.create((FluxSink<ServerSentEvent<Object>> fluxSink) -> {
                    ReactiveSseCreation creation = new ReactiveSseCreation(id, path, fluxSink);
                    //fluxSink.onCancel(() -> {});
                    fluxSink.onDispose(() -> concept.onClose(id, Connection.Type.CLIENT, null));
                    concept.onEstablish(creation, params);
                }))
                .doOnError(it -> concept.onError(id, Connection.Type.CLIENT, it))
                //.doOnComplete(() -> concept.onClose(id, Connection.Type.CLIENT, null))
                .doAfterTerminate(() -> concept.onClose(id, Connection.Type.CLIENT, null));
    }
}
