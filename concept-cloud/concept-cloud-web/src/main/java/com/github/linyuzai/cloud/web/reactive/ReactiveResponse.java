package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.concept.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Getter
@RequiredArgsConstructor
public class ReactiveResponse implements Response {

    private final ServerHttpResponse response;
}
