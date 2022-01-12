package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import reactor.core.publisher.Mono;

/**
 * 请求提供者 / Provider of request
 */
public interface DownloadRequestProvider {

    /**
     * 获得请求 / Get request
     *
     * @param context 下载上下文 / Context of download
     * @return 请求 / Request
     */
    Mono<DownloadRequest> getRequest(DownloadContext context);
}
