package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.web.DownloadRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 持有 {@link ServerHttpRequest} 的 {@link DownloadRequest}，用于 webflux。
 */
@Getter
@AllArgsConstructor
public class ReactiveDownloadRequest implements DownloadRequest {

    private ServerHttpRequest request;

    @Override
    public String getHeader(String name) {
        return request.getHeaders().getFirst(name);
    }
}
