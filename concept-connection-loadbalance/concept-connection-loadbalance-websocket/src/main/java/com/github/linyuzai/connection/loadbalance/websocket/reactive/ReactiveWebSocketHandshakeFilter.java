package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketHandshakeInterceptor;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequest;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ReactiveWebSocketHandshakeFilter implements WebFilter {

    private final List<WebSocketHandshakeInterceptor> handshakeInterceptors;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Map<String, Object> attributes = exchange.getAttributes();
        WebSocketRequest req = new ReactiveWebSocketRequest(exchange.getRequest());
        WebSocketResponse resp = new ReactiveWebSocketResponse(exchange.getResponse());
        for (WebSocketHandshakeInterceptor interceptor : handshakeInterceptors) {
            if (!interceptor.onHandshake(req, resp, attributes)) {
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }
}
