package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * {@link DownloadContext} 初始化器，{@link DownloadContext} 初始化时会回调。
 */
public interface DownloadContextInitializer extends OrderProvider {

    /**
     * 初始化。
     * <p>
     * initialize.
     *
     * @param context {@link DownloadContext}
     */
    void initialize(DownloadContext context);
}
