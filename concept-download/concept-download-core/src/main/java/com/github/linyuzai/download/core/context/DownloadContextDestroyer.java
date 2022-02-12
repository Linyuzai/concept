package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * {@link DownloadContext} 销毁器，{@link DownloadContext} 销毁时会回调。
 */
public interface DownloadContextDestroyer extends OrderProvider {

    /**
     * 销毁。
     *
     * @param context {@link DownloadContext}
     */
    void destroy(DownloadContext context);
}
