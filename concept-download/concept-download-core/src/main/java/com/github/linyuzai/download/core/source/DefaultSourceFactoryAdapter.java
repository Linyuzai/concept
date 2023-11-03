package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * {@link SourceFactoryAdapter} 的默认实现。
 */
@Getter
@RequiredArgsConstructor
public class DefaultSourceFactoryAdapter implements SourceFactoryAdapter {

    /**
     * {@link SourceFactory} 列表
     */
    private final List<SourceFactory> factories;

    /**
     * 获得适配的 {@link SourceFactory}，
     * 如果没有可用的 {@link SourceFactory} 则抛出异常。
     *
     * @param source  需要下载的原始数据对象
     * @param context {@link DownloadContext}
     * @return 匹配上的 {@link SourceFactory}
     */
    @Override
    public SourceFactory getFactory(Object source, DownloadContext context) {
        for (SourceFactory factory : factories) {
            if (factory.support(source, context)) {
                return factory;
            }
        }
        throw new DownloadException("No SourceFactory support: " + source);
    }
}
