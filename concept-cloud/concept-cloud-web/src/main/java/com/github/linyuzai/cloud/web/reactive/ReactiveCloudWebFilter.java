package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * 用于设置当前的请求和响应。
 */
@RequiredArgsConstructor
public class ReactiveCloudWebFilter implements WebFilter {

    /**
     * 版本兼容
     */
    private static boolean hasMethod;

    static {
        try {
            hasMethod = Mono.class.getMethod("contextWrite", Function.class) != null;
        } catch (Throwable ignore) {
        }
    }

    private final RequestMappingHandlerMapping handlerMapping;

    private final WebContextFactory contextFactory;

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        WebContext context = contextFactory.create();
        context.put(ServerWebExchange.class, exchange);
        context.put(ServerHttpRequest.class, exchange.getRequest());
        context.put(ServerHttpResponse.class, exchange.getResponse());
        context.put(WebContext.Request.PATH, exchange.getRequest().getPath().value());
        Mono<Void> mono = handlerMapping.getHandler(exchange)
                //.switchIfEmpty(chain.filter(exchange))
                .flatMap(it -> {
                    if (it instanceof HandlerMethod) {
                        context.put(HandlerMethod.class, it);
                    }
                    return chain.filter(exchange);
                });
        if (hasMethod) {
            return mono.contextWrite(ctx -> ctx.put(WebContext.class, context));
        } else {
            return mono.subscriberContext(ctx -> ctx.put(WebContext.class, context));
        }
    }
}