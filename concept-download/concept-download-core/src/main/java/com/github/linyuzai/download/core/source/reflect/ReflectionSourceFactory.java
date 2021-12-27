package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持反射的工厂 / Factory support reflection
 */
public class ReflectionSourceFactory implements SourceFactory {

    private final Map<Class<?>, ReflectionTemplate> reflectionTemplateMap = new ConcurrentHashMap<>();

    /**
     * 类上是否标记了 {@link SourceModel} / If {@link SourceModel} marked on the class
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / Is it supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isAnnotationPresent(SourceModel.class);
    }

    /**
     * 对模型进行注解解析 / Parse the annotation on model
     * 获得对应的下载源 / Get the source {@link SourceObject}
     * 基于下载源进行适配 / Adaptation based on source
     * 将一些属性通过反射重新设置 / Reset some attributes through reflection
     *
     * @param model   模型 / Model
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source
     */
    @Override
    public Source create(Object model, DownloadContext context) {
        ReflectionTemplate template = reflectionTemplateMap.computeIfAbsent(model.getClass(), this::newTemplate);
        Object reflect = template.value(SourceObject.class, model);
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        SourceFactory factory = adapter.getFactory(reflect, context);
        Source source = factory.create(reflect, context);
        template.reflect(model, source);
        return source;
    }

    protected ReflectionTemplate newTemplate(Class<?> clazz) {
        return new ReflectionTemplate(clazz);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
