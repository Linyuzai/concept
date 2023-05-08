package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.concept.Request;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Getter
@RequiredArgsConstructor
public class ReactiveRequest implements Request {

    private final ServerHttpRequest request;

    @Override
    public String getMethod() {
        return request.getMethodValue();
    }

    @Override
    public String getPath() {
        return request.getPath().value();
    }

    @Override
    public String getHeader(String name) {
        return request.getHeaders().getFirst(name);
    }
}
