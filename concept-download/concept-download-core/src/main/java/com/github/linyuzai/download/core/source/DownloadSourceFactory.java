package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface DownloadSourceFactory {

    /**
     * 是否能加载
     *
     * @param source  需要下载的数据对象
     * @param context 下载上下文
     * @return 是否能加载
     */
    boolean support(Object source, DownloadContext context);

    /**
     * 加载
     *
     * @param source  需要下载的数据对象
     * @param context 下载上下文
     * @return 下载源
     */
    DownloadSource create(Object source, DownloadContext context);

    default int getOrder() {
        return 0;
    }
}
