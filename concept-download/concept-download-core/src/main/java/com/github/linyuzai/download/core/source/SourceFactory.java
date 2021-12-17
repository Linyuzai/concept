package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;

/**
 * 数据源工厂 / Factory of download source
 */
public interface SourceFactory extends OrderProvider {

    /**
     * 是否支持某个对象 / Whether an object is supported
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / If supported
     */
    boolean support(Object source, DownloadContext context);

    /**
     * 创建 / Create
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source
     */
    Source create(Object source, DownloadContext context);
}
