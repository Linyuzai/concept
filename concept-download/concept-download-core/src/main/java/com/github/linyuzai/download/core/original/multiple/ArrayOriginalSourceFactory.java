package com.github.linyuzai.download.core.original.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayOriginalSourceFactory implements OriginalSourceFactory {

    private final OriginalSourceFactory factory = new CollectionOriginalSourceFactory();

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isArray();
    }

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        List<Object> objects = new ArrayList<>();
        int length = Array.getLength(source);
        for (int i = 0; i < length; i++) {
            Object o = Array.get(source, i);
            objects.add(o);
        }
        return factory.create(objects, context);
    }
}
