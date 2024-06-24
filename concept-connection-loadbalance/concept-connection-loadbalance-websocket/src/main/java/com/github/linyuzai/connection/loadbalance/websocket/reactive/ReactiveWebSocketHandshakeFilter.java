package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequest;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequestInterceptor;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Deprecated
@Getter
@RequiredArgsConstructor
public class ReactiveWebSocketHandshakeFilter implements WebFilter {

    private final List<WebSocketRequestInterceptor> interceptors;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (HttpMethod.GET.equals(request.getMethod()) &&
                "websocket".equalsIgnoreCase(request.getHeaders().getUpgrade())) {
            WebSocketRequest req = new ReactiveWebSocketRequest(request);
            WebSocketResponse resp = new ReactiveWebSocketResponse(exchange.getResponse());
            for (WebSocketRequestInterceptor interceptor : interceptors) {
                if (interceptor.intercept(req, resp)) {
                    return Mono.empty();
                }
            }
        }
        return chain.filter(exchange);
    }
}
