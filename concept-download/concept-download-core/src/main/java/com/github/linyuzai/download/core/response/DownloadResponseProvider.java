package com.github.linyuzai.download.core.response;

import com.github.linyuzai.download.core.context.DownloadContext;

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
    DownloadResponse getResponse(DownloadContext context);
}
