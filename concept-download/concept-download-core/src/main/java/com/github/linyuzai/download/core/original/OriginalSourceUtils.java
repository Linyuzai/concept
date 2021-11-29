package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.util.Collection;

public class OriginalSourceUtils {

    public static OriginalSource create(Object source, DownloadContext context, Collection<OriginalSourceFactory> factories) {
        OriginalSourceFactory factory = getOriginalSourceFactory(source, context, factories);
        if (factory == null) {
            throw new OriginalSourceException("No OriginalSourceFactory support: " + source);
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
    public static OriginalSourceFactory getOriginalSourceFactory(Object source, DownloadContext context, Collection<OriginalSourceFactory> factories) {
        for (OriginalSourceFactory factory : factories) {
            if (factory.support(source, context)) {
                return factory;
            }
        }
        return null;
    }
}
