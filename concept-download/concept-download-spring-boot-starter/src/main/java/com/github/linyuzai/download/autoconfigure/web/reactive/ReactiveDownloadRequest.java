package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.web.DownloadRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 持有 {@link ServerHttpRequest} 的 {@link DownloadRequest}，用于 webflux。
 */
@Getter
@RequiredArgsConstructor
public class ReactiveDownloadRequest implements DownloadRequest {

    private final ServerHttpRequest request;

    @Override
    public String getHeader(String name) {
        return request.getHeaders().getFirst(name);
    }
}
