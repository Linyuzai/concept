package com.github.linyuzai.download.web.reactive.request;

import com.github.linyuzai.download.core.request.DownloadRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * ServerHttpRequest实现 / implementations by ServerHttpRequest
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
