package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * 数据源工厂的适配器 / Adapter of source factory
 */
public interface SourceFactoryAdapter {

    /**
     * 获得适配的工厂 / Get suitable factory
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 数据源工厂 / Factory of source
     */
    SourceFactory getFactory(Object source, DownloadContext context);
}
