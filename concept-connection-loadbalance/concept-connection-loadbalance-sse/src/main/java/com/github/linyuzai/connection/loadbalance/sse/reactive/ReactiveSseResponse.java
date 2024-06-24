package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Getter
@RequiredArgsConstructor
public class ReactiveSseResponse implements SseResponse {

    private final ServerHttpResponse response;

    @Override
    public boolean setStatusCode(HttpStatus status) {
        return response.setStatusCode(status);
    }

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }
}
