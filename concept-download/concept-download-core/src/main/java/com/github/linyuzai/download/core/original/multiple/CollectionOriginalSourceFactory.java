package com.github.linyuzai.download.core.original.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;
import com.github.linyuzai.download.core.original.OriginalSourceFactoryAdapter;

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
        OriginalSourceFactoryAdapter adapter = context.get(OriginalSourceFactoryAdapter.class);
        List<OriginalSource> sources = new ArrayList<>();
        for (Object o : ((Collection<?>) source)) {
            OriginalSourceFactory factory = adapter.getOriginalSourceFactory(o, context);
            sources.add(factory.create(o, context));
        }
        return new MultipleOriginalSource(sources);
    }


}
