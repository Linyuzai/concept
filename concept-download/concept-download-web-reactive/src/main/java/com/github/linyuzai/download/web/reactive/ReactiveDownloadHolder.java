package com.github.linyuzai.download.web.reactive;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.Function;

@Deprecated
public class ReactiveDownloadHolder {

    public static ServerHttpRequest getRequest() {
        try {
            return Mono.subscriberContext().map(ctx -> ctx.get(ServerHttpRequest.class)).toProcessor().block();
        } catch (Throwable ignore) {
        }
        try {
            return Mono.deferContextual((Function<ContextView, Mono<ServerHttpRequest>>) contextView ->
                    Mono.justOrEmpty(contextView.get(ServerHttpRequest.class))).share().block();
        } catch (Throwable ignore) {
        }
        return null;
    }

    public static ServerHttpResponse getResponse() {
        try {
            return Mono.subscriberContext().map(ctx -> ctx.get(ServerHttpResponse.class)).toProcessor().block();
        } catch (Throwable ignore) {
        }
        try {
            return Mono.deferContextual((Function<ContextView, Mono<ServerHttpResponse>>) contextView ->
                    Mono.justOrEmpty(contextView.get(ServerHttpResponse.class))).share().block();
        } catch (Throwable ignore) {
        }
        return null;
    }
}
