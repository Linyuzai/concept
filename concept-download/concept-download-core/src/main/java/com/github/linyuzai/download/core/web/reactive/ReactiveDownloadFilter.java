package com.github.linyuzai.download.core.web.reactive;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class ReactiveDownloadFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        try {
            return chain.filter(exchange)
                    .contextWrite(ctx -> ctx.put(ServerHttpRequest.class, request))
                    .contextWrite(ctx -> ctx.put(ServerHttpResponse.class, response));
        } catch (Throwable ignore) {
        }
        try {
            return chain.filter(exchange)
                    .subscriberContext(ctx -> ctx.put(ServerHttpRequest.class, request))
                    .subscriberContext(ctx -> ctx.put(ServerHttpResponse.class, response));
        } catch (Throwable ignore) {
        }
        return chain.filter(exchange);
    }
}