package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * 响应提供者 / Provider of response
 */
public interface DownloadResponseProvider {

    /**
     * 获得响应 / Get response
     *
     * @param context 下载上下文 / Context of download
     * @return 响应 / Response
     */
    Mono<DownloadResponse> getResponse(DownloadContext context);
}
