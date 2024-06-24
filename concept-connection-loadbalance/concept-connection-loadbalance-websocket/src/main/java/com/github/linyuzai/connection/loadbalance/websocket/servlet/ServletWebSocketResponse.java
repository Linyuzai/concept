package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;

@Getter
@RequiredArgsConstructor
public class ServletWebSocketResponse implements WebSocketResponse {

    private final ServerHttpResponse response;

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    @Override
    public boolean setStatusCode(HttpStatus status) {
        response.setStatusCode(status);
        return true;
    }
}
