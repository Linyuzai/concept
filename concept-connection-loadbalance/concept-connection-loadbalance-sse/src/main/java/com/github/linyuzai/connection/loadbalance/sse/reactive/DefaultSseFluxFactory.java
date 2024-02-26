package com.github.linyuzai.connection.loadbalance.sse.reactive;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public class DefaultSseFluxFactory implements SseFluxFactory {

    @Override
    public Flux<ServerSentEvent<Object>> create(Flux<ServerSentEvent<Object>> flux) {
        return flux;
    }
}
