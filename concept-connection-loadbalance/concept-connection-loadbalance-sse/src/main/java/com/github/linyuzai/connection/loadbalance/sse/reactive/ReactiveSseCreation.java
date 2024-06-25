package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseCreation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

@Getter
@RequiredArgsConstructor
public class ReactiveSseCreation implements SseCreation {

    private final Object id;

    private final String path;

    /**
     * 发送事件的 sink
     */
    private final FluxSink<ServerSentEvent<Object>> fluxSink;
}
