package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionSourceFactory implements SourceFactory {

    private final Map<Class<?>, ReflectionTemplate> reflectionTemplateMap = new ConcurrentHashMap<>();

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isAnnotationPresent(SourceModel.class);
    }

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
