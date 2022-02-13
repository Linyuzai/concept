package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * {@link DownloadResponse} 的提供者。
 */
public interface DownloadResponseProvider {

    /**
     * 获得 {@link DownloadResponse}。
     *
     * @param context {@link DownloadContext}
     * @return {@link DownloadResponse} 对应的 {@link Mono}
     */
    Mono<DownloadResponse> getResponse(DownloadContext context);
}
