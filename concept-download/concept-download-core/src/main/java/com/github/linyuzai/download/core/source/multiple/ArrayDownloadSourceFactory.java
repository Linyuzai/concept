package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;
import com.github.linyuzai.download.core.source.DownloadSourceFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayDownloadSourceFactory implements DownloadSourceFactory {

    private final DownloadSourceFactory factory = new CollectionDownloadSourceFactory();

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isArray();
    }

    @Override
    public DownloadSource create(Object source, DownloadContext context) {
        List<Object> objects = new ArrayList<>();
        int length = Array.getLength(source);
        for (int i = 0; i < length; i++) {
            Object o = Array.get(source, i);
            objects.add(o);
        }
        return factory.create(objects, context);
    }
}
