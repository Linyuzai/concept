package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseTimeoutException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Getter
@Setter
@RestController
@RequestMapping("${concept.sse.load-balance.observable-endpoint:concept-sse-subscriber}")
@RequiredArgsConstructor
public class ServletSseLoadBalanceEndpoint {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private SseEmitterFactory sseEmitterFactory = new DefaultSseEmitterFactory();

    private final SseLoadBalanceConcept concept;

    private final String endpoint;

    @GetMapping
    public SseEmitter loadBalanceEndpoint(@RequestParam Map<Object, Object> params) {
        Object id = sseIdGenerator.generateId(params);
        SseEmitter emitter = sseEmitterFactory.create();
        ServletSseCreateRequest request = new ServletSseCreateRequest(id, endpoint, emitter);
        ServletSseConnection connection = new ServletSseConnection(emitter);
        connection.setCreateRequest(request);
        connection.setType(Connection.Type.OBSERVABLE);
        connection.getMetadata().putAll(params);
        emitter.onError(e -> concept.onError(connection, e));
        emitter.onCompletion(() -> concept.onClose(connection, null));
        emitter.onTimeout(() -> concept.onError(connection, new SseTimeoutException("SSE Timeout")));
        concept.onEstablish(connection);
        return emitter;
    }
}
