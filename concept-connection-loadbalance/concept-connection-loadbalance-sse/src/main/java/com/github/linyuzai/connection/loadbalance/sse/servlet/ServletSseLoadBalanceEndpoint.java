package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT)
@RequiredArgsConstructor
public class ServletSseLoadBalanceEndpoint {

    private final SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private final SseEmitterFactory sseEmitterFactory = new DefaultSseEmitterFactory();

    private final SseLoadBalanceConcept concept;

    @GetMapping("{path}")
    public SseEmitter defaultEndpoint(@PathVariable String path) {
        Object id = sseIdGenerator.generateId(null);
        SseEmitter emitter = sseEmitterFactory.create();
        ServletSseCreateRequest request = new ServletSseCreateRequest(id, path, emitter);
        ServletSseConnection connection = new ServletSseConnection(emitter);
        connection.setCreateRequest(request);
        connection.setType(Connection.Type.OBSERVABLE);
        emitter.onError(e -> concept.onError(connection, e));
        emitter.onCompletion(() -> concept.onClose(connection, null));
        emitter.onTimeout(() -> concept.onError(connection, new SseTimeoutException("SSE Timeout")));
        concept.onEstablish(connection);
        return emitter;
    }
}
