package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * 下载处理器。
 */
public interface DownloadHandler {

    int ORDER_LOOKUP_CACHE = 0;
    int ORDER_CREATE_SOURCE = 100;
    int ORDER_LOAD_SOURCE = 200;
    int ORDER_COMPRESS_SOURCE = 300;
    int ORDER_WRITE_RESPONSE = 400;

    /**
     * 执行处理。
     *
     * @param context {@link DownloadContext}
     * @param chain   {@link DownloadHandlerChain}
     */
    Object handle(DownloadContext context, DownloadHandlerChain chain);

    default boolean support(DownloadContext context) {
        return true;
    }

    /**
     * 默认返回 0。
     *
     * @return 0
     */
    default int getOrder() {
        return 0;
    }
}
