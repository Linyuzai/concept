package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@Setter
@RequiredArgsConstructor
public class ServletSseCreateRequest implements SseCreateRequest {

    private final Object id;

    private final String path;

    private final SseEmitter sseEmitter;
}
