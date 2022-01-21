package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * 上下文初始化器 / Initializer of context
 */
public interface DownloadContextInitializer extends OrderProvider {

    /**
     * 初始化处理 / init
     *
     * @param context 下载上下文 / Context of download
     */
    void initialize(DownloadContext context);
}
