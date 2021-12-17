package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 数组类型的下载源工厂 / Factory of array
 */
public class ArraySourceFactory implements SourceFactory {

    private final SourceFactory factory = new CollectionSourceFactory();

    /**
     * 支持数组 / Array supported
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / If supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isArray();
    }

    /**
     * 将数组转为集合 / Convert array to collection
     * 交给集合类型的工厂处理 / To the factory of the collection type {@link CollectionSourceFactory}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source
     */
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
