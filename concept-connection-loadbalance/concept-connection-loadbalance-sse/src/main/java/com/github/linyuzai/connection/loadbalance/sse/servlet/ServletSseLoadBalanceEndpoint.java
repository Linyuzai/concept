package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer.LB_HOST_PORT;

@Getter
@Setter
@RestController
@RequestMapping(SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT)
@RequiredArgsConstructor
public class ServletSseLoadBalanceEndpoint {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private SseEmitterFactory sseEmitterFactory = new DefaultSseEmitterFactory();

    private final SseLoadBalanceConcept concept;

    @GetMapping
    public SseEmitter loadBalanceEndpoint(@RequestParam(LB_HOST_PORT) String lbHostPort) {
        Map<Object,Object> metadata = new LinkedHashMap<>();
        metadata.put(LB_HOST_PORT, lbHostPort);
        Object id = sseIdGenerator.generateId(metadata);
        SseEmitter emitter = sseEmitterFactory.create();
        ServletSseCreateRequest request = new ServletSseCreateRequest(id, SseLoadBalanceConcept.SUBSCRIBER_ENDPOINT, emitter);
        ServletSseConnection connection = new ServletSseConnection(emitter);
        connection.setCreateRequest(request);
        connection.setType(Connection.Type.OBSERVABLE);
        connection.setMetadata(metadata);
        emitter.onError(e -> concept.onError(connection, e));
        emitter.onCompletion(() -> concept.onClose(connection, null));
        emitter.onTimeout(() -> concept.onError(connection, new SseTimeoutException("SSE Timeout")));
        concept.onEstablish(connection);
        return emitter;
    }
}
