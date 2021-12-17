package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合类型的下载源工厂 / Factory of collection
 */
public class CollectionSourceFactory implements SourceFactory {

    /**
     * 支持集合 / Collection supported
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / If supported
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
    public Source create(Object source, DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        List<Source> sources = new ArrayList<>();
        for (Object o : ((Collection<?>) source)) {
            SourceFactory factory = adapter.getFactory(o, context);
            sources.add(factory.create(o, context));
        }
        return new MultipleSource(sources);
    }
}
