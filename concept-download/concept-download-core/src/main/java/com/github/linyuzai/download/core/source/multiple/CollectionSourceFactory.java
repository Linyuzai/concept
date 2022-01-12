package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 支持集合类型的工厂 / Factory support collection
 */
public class CollectionSourceFactory implements SourceFactory {

    /**
     * 支持集合 / Collection supported
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / Is it supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Collection;
    }

    /**
     * Use {@link MultipleSource}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source {@link MultipleSource}
     */
    @Override
    public Mono<Source> create(Object source, DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        return Flux.fromIterable((Collection<?>) source)
                .flatMap(it -> adapter.getFactory(it, context).create(it, context)).collectList()
                .map(MultipleSource::new);
    }
}
