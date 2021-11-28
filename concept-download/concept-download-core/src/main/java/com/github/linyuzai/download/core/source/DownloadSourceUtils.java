package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.util.Collection;

public class DownloadSourceUtils {

    public static DownloadSource create(Object source, DownloadContext context, Collection<DownloadSourceFactory> factories) {
        DownloadSourceFactory factory = getDownloadSourceFactory(source, context, factories);
        if (factory == null) {
            throw new DownloadSourceException("No DownloadSourceFactory support: " + source);
        }
        return factory.create(source, context);
    }

    /**
     * 为每个需要下载的数据对象都匹配对应的加载器，并加载返回下载源
     *
     * @param source  需要下载的数据对象
     * @param context 下载上下文
     * @return 下载源
     */
    public static DownloadSourceFactory getDownloadSourceFactory(Object source, DownloadContext context, Collection<DownloadSourceFactory> factories) {
        for (DownloadSourceFactory factory : factories) {
            if (factory.support(source, context)) {
                return factory;
            }
        }
        return null;
    }
}
