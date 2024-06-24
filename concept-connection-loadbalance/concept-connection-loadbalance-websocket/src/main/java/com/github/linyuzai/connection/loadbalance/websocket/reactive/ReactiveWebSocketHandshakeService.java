package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequest;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequestInterceptor;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReactiveWebSocketHandshakeService extends HandshakeWebSocketService {

    private final List<WebSocketRequestInterceptor> interceptors;

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler handler) {
        WebSocketRequest req = new ReactiveWebSocketRequest(exchange.getRequest());
        WebSocketResponse resp = new ReactiveWebSocketResponse(exchange.getResponse());
        for (WebSocketRequestInterceptor interceptor : interceptors) {
            if (interceptor.intercept(req, resp)) {
                return Mono.empty();
            }
        }
        return super.handleRequest(exchange, handler);
    }
}
