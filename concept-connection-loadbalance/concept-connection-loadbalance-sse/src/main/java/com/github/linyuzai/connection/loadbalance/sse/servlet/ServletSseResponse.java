package com.github.linyuzai.connection.loadbalance.sse.servlet;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;

@Getter
@RequiredArgsConstructor
public class ServletSseResponse implements SseResponse {

    private final ServerHttpResponse response;

    @Override
    public boolean setStatusCode(HttpStatus status) {
        response.setStatusCode(status);
        return true;
    }

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }
}
