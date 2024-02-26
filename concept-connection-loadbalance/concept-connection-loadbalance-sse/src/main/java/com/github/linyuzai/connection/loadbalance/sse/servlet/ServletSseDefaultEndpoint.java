package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnection;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("${concept.sse.server.default-endpoint.prefix:concept-sse}")
@RequiredArgsConstructor
public class ServletSseDefaultEndpoint {

    private final SseIdGenerator sseIdGenerator;

    private final SseEmitterFactory sseEmitterFactory;

    private final SseLoadBalanceConcept concept;

    @GetMapping("{path}")
    public SseEmitter defaultEndpoint(@PathVariable String path, @RequestParam Map<Object, Object> params) {
        Object id = sseIdGenerator.generateId(params);
        SseEmitter emitter = sseEmitterFactory.create();
        ServletSseCreateRequest request = new ServletSseCreateRequest(id, path, emitter);
        SseConnection connection = (SseConnection) concept.createConnection(request, params);
        emitter.onError(e -> concept.onError(connection, e));
        emitter.onCompletion(() -> concept.onClose(connection, null));
        emitter.onTimeout(() -> concept.onError(connection, new SseTimeoutException("SSE Timeout")));
        concept.onEstablish(connection);
        return emitter;
    }
}
