package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Getter
@RequiredArgsConstructor
public class ReactiveWebSocketResponse implements WebSocketResponse {

    private final ServerHttpResponse response;

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    @Override
    public boolean setStatusCode(HttpStatus status) {
        return response.setStatusCode(status);
    }
}
