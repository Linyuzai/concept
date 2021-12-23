package com.github.linyuzai.download.core.source.reflection;

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
    public Source create(Object source, DownloadContext context) {
        ReflectionTemplate template = reflectionTemplateMap.computeIfAbsent(source.getClass(), ReflectionTemplate::new);
        Object reflect = template.reflect(SourceObject.class, source);
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        SourceFactory factory = adapter.getFactory(reflect, context);
        return new ReflectionSource(template, source, factory.create(reflect, context));
    }

    /*private ReflectionTemplate reflect(Class<?> clazz) {
        ReflectionTemplate template = new ReflectionTemplate(clazz);
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            ReflectionTemplate superTemplate = reflect(superclass);
            template.mergeSuper(superTemplate);
        }
        return template;
    }*/

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
