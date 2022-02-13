package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * {@link Source} 工厂。
 */
public interface SourceFactory extends OrderProvider {

    /**
     * 是否支持需要下载的原始数据对象。
     *
     * @param source  需要下载的原始数据对象
     * @param context {@link DownloadContext}
     * @return 如果支持则返回 true
     */
    boolean support(Object source, DownloadContext context);

    /**
     * 创建。
     *
     * @param source  需要下载的原始数据对象
     * @param context {@link DownloadContext}
     * @return 创建的 {@link Source}
     */
    Source create(Object source, DownloadContext context);
}
