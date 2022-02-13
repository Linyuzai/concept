package com.github.linyuzai.download.core.web.reactive;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * 用于获得当前的请求和响应。
 *
 * @see ReactiveDownloadFilter
 */
public class ReactiveDownloadHolder {

    /**
     * 版本兼容
     */
    private static boolean hasMethod;

    static {
        try {
            hasMethod = Mono.class.getMethod("deferContextual", Function.class) != null;
        } catch (Throwable ignore) {
        }
    }

    public static Mono<ServerHttpRequest> getRequest() {
        if (hasMethod) {
            return Mono.deferContextual(contextView -> Mono.just(contextView.get(ServerHttpRequest.class)));
        } else {
            return Mono.subscriberContext().map(ctx -> ctx.get(ServerHttpRequest.class));
        }
    }

    public static Mono<ServerHttpResponse> getResponse() {
        if (hasMethod) {
            return Mono.deferContextual(contextView -> Mono.just(contextView.get(ServerHttpResponse.class)));
        } else {
            return Mono.subscriberContext().map(ctx -> ctx.get(ServerHttpResponse.class));
        }
    }
}
