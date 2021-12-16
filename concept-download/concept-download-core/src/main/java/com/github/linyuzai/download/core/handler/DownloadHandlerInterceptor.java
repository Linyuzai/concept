package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * 下载处理器拦截器 / Interceptor of handler
 */
public interface DownloadHandlerInterceptor {

    /**
     * 每一个处理器处理完后进行拦截 / Each handler intercepts after handled
     *
     * @param handler 下载处理器 / Handler of download
     * @param context 下载上下文 / Context of download
     */
    void intercept(DownloadHandler handler, DownloadContext context);
}
