package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;
import com.github.linyuzai.download.core.source.DownloadSourceFactory;
import com.github.linyuzai.download.core.source.DownloadSourceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionDownloadSourceFactory implements DownloadSourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Collection;
    }

    @Override
    public DownloadSource create(Object source, DownloadContext context) {
        Collection<DownloadSourceFactory> factories = context.get(DownloadSourceFactory.class);
        List<DownloadSource> sources = new ArrayList<>();
        for (Object o : ((Collection<?>) source)) {
            sources.add(DownloadSourceUtils.create(o, context, factories));
        }
        return new MultipleDownloadSource(sources);
    }


}
