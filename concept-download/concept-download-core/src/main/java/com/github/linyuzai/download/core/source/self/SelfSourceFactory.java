package com.github.linyuzai.download.core.source.self;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import reactor.core.publisher.Mono;

/**
 * 本身就是下载源的工厂 / Factory of source itself
 */
public class SelfSourceFactory implements SourceFactory {

    /**
     * 支持Source对象 / Support Source instance
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / Is it supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Source;
    }

    /**
     * 直接返回本身 / Return itself directly
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source
     */
    @Override
    public Mono<Source> create(Object source, DownloadContext context) {
        return Mono.just((Source) source);
    }
}
