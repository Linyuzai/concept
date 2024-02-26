package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

@Getter
@RequiredArgsConstructor
public class ReactiveSseCreateRequest implements SseCreateRequest {

    private final Object id;

    private final String path;

    private final FluxSink<ServerSentEvent<Object>> fluxSink;
}
