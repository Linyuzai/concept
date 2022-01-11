package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.concept.DownloadFunction;
import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;

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
    DownloadRequest getRequest(DownloadContext context);

    default Object getRequest(DownloadContext context, DownloadFunction<DownloadRequest, Object> function) throws IOException {
        return function.apply(getRequest(context));
    }
}
