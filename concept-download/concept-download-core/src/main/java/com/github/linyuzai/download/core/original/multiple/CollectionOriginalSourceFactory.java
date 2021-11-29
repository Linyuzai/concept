package com.github.linyuzai.download.core.original.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;
import com.github.linyuzai.download.core.original.OriginalSourceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionOriginalSourceFactory implements OriginalSourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Collection;
    }

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        Collection<OriginalSourceFactory> factories = context.get(OriginalSourceFactory.class);
        List<OriginalSource> sources = new ArrayList<>();
        for (Object o : ((Collection<?>) source)) {
            sources.add(OriginalSourceUtils.create(o, context, factories));
        }
        return new MultipleOriginalSource(sources);
    }


}
