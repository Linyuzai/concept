package com.github.linyuzai.download.core.web.reactive;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.Function;

public class ReactiveDownloadHolder {

    public static Mono<ServerHttpRequest> getRequest() {
        try {
            return Mono.deferContextual((Function<ContextView, Mono<ServerHttpRequest>>) contextView ->
                    Mono.justOrEmpty(contextView.get(ServerHttpRequest.class)));
        } catch (Throwable ignore) {
        }
        try {
            return Mono.subscriberContext().map(ctx -> ctx.get(ServerHttpRequest.class));
        } catch (Throwable ignore) {
        }
        return null;
    }

    public static Mono<ServerHttpResponse> getResponse() {
        try {
            return Mono.deferContextual((Function<ContextView, Mono<ServerHttpResponse>>) contextView ->
                    Mono.justOrEmpty(contextView.get(ServerHttpResponse.class)));
        } catch (Throwable ignore) {
        }
        try {
            return Mono.subscriberContext().map(ctx -> ctx.get(ServerHttpResponse.class));
        } catch (Throwable ignore) {
        }
        return null;
    }
}
