package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 匹配数组的 {@link SourceFactory}。
 */
public class ArraySourceFactory implements SourceFactory {

    private final SourceFactory factory = new CollectionSourceFactory();

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isArray();
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        List<Object> objects = new ArrayList<>();
        int length = Array.getLength(source);
        for (int i = 0; i < length; i++) {
            Object o = Array.get(source, i);
            objects.add(o);
        }
        return factory.create(objects, context);
    }
}
