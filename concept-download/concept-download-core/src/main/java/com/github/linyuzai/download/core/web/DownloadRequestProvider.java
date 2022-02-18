package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * {@link DownloadRequest} 的提供者。
 */
public interface DownloadRequestProvider {

    /**
     * 获得 {@link DownloadRequest} 对应的 {@link Mono}。
     *
     * @param context {@link DownloadContext}
     * @return {@link DownloadRequest} 对应的 {@link Mono}
     */
    Mono<DownloadRequest> getRequest(DownloadContext context);
}
