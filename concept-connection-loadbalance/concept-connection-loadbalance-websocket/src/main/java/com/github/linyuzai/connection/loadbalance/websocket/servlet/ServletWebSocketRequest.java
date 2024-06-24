package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServerHttpRequest;

import java.net.InetSocketAddress;
import java.net.URI;

@Getter
@RequiredArgsConstructor
public class ServletWebSocketRequest implements WebSocketRequest {

    private final ServerHttpRequest request;

    @Override
    public HttpMethod getMethod() {
        return request.getMethod();
    }

    @Override
    public String getMethodValue() {
        return request.getMethodValue();
    }

    @Override
    public URI getURI() {
        return request.getURI();
    }

    @Override
    public HttpHeaders getHeaders() {
        return request.getHeaders();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return request.getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return request.getRemoteAddress();
    }
}
