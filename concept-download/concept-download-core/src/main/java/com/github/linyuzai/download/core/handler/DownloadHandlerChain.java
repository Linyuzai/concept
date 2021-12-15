package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;

/**
 * 下载拦截链
 */
public interface DownloadHandlerChain {

    /**
     * 执行下一个下载拦截器
     *
     * @param context 下载上下文
     */
    void next(DownloadContext context) throws IOException;
}
