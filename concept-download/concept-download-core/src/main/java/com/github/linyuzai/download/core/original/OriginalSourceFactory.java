package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;

public interface OriginalSourceFactory extends OrderProvider {

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
    OriginalSource create(Object source, DownloadContext context);
}
