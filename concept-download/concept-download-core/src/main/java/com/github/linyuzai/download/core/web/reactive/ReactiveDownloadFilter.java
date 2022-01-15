package com.github.linyuzai.download.core.web.reactive;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ReactiveDownloadFilter implements WebFilter {

    private static boolean hasMethod;

    static {
        try {
            hasMethod = Mono.class.getMethod("contextWrite", Function.class) != null;
        } catch (Throwable ignore) {
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (hasMethod) {
            return chain.filter(exchange)
                    .contextWrite(ctx -> ctx.put(ServerHttpRequest.class, request))
                    .contextWrite(ctx -> ctx.put(ServerHttpResponse.class, response));
        } else {
            return chain.filter(exchange)
                    .subscriberContext(ctx -> ctx.put(ServerHttpRequest.class, request))
                    .subscriberContext(ctx -> ctx.put(ServerHttpResponse.class, response));
        }
    }
}