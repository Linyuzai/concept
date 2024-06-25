package com.github.linyuzai.connection.loadbalance.sse.reactive;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

/**
 * SSE 事件发射器工厂。
 * <p>
 * Factory of SSE events flux.
 */
public interface SseFluxFactory {

    Flux<ServerSentEvent<Object>> create(Flux<ServerSentEvent<Object>> flux);
}
