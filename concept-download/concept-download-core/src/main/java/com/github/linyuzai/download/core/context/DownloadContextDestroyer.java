package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * {@link DownloadContext} 销毁器。
 * <p>
 * Destroyer of {@link DownloadContext}.
 */
public interface DownloadContextDestroyer extends OrderProvider {

    /**
     * 销毁。
     * <p>
     * Destroy.
     *
     * @param context {@link DownloadContext}
     */
    void destroy(DownloadContext context);
}
