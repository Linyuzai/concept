package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * {@link SourceFactory} 的适配器。
 */
public interface SourceFactoryAdapter {

    /**
     * 获得适配的 {@link SourceFactory}。
     *
     * @param source  需要下载的原始数据对象
     * @param context {@link DownloadContext}
     * @return 匹配上的 {@link SourceFactory}
     */
    SourceFactory getFactory(Object source, DownloadContext context);
}
