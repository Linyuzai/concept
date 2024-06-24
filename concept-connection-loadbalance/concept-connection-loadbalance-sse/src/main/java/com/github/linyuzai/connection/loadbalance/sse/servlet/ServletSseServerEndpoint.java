package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.sse.concept.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${concept.sse.server.default-endpoint.prefix:concept-sse}")
@RequiredArgsConstructor
public class ServletSseServerEndpoint {

    private final SseIdGenerator sseIdGenerator;

    private final SseEmitterFactory sseEmitterFactory;

    private final SseLoadBalanceConcept concept;

    private final List<SseRequestInterceptor> interceptors;

    @GetMapping("{path}")
    public SseEmitter defaultEndpoint(@PathVariable String path,
                                      @RequestParam Map<Object, Object> params,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        SseRequest req = new ServletSseRequest(new ServletServerHttpRequest(request));
        SseResponse resp = new ServletSseResponse(new ServletServerHttpResponse(response));
        for (SseRequestInterceptor interceptor : interceptors) {
            if (interceptor.intercept(req, resp)) {
                return null;
            }
        }
        Object id = sseIdGenerator.generateId(params);
        SseEmitter emitter = sseEmitterFactory.create();
        ServletSseCreation creation = new ServletSseCreation(id, path, emitter);
        Connection connection = concept.createConnection(creation, params);
        emitter.onError(e -> concept.onError(connection, e));
        emitter.onCompletion(() -> concept.onClose(connection, null));
        emitter.onTimeout(() -> concept.onError(connection, new SseTimeoutException("SSE Timeout")));
        concept.onEstablish(connection);
        return emitter;
    }
}
