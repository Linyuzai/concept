package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.concept.Request;
import com.github.linyuzai.cloud.web.core.concept.Response;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 用于设置上下文
 */
@RequiredArgsConstructor
public class ReactiveCloudWebFilter implements WebFilter {

    //private final RequestMappingHandlerMapping handlerMapping;

    private final WebContextFactory contextFactory;

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        WebContext context = contextFactory.create();
        context.put(ServerWebExchange.class, exchange);
        context.put(ServerHttpRequest.class, exchange.getRequest());
        context.put(ServerHttpResponse.class, exchange.getResponse());
        context.put(Request.class, new ReactiveRequest(exchange.getRequest()));
        context.put(Response.class, new ReactiveResponse(exchange.getResponse()));
        return chain.filter(exchange).contextWrite(ctx -> ctx.put(WebContext.class, context));
        /*return handlerMapping.getHandler(exchange)
                //.switchIfEmpty(chain.filter(exchange))
                .flatMap(it -> {
                    if (it instanceof HandlerMethod) {
                        context.put(HandlerMethod.class, it);
                    }
                    return chain.filter(exchange);
                }).contextWrite(ctx -> ctx.put(WebContext.class, context));*/
    }
}