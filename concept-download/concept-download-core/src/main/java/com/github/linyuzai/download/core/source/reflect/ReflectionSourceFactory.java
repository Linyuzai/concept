package com.github.linyuzai.download.core.source.reflect;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持反射 {@link SourceFactory}。
 */
public class ReflectionSourceFactory implements SourceFactory {

    private final Map<Class<?>, ReflectionTemplate> reflectionTemplateMap = new ConcurrentHashMap<>();

    /**
     * 类上是否标记了 {@link SourceModel}。
     *
     * @param source  需要下载的原始数据对象
     * @param context {@link DownloadContext}
     * @return 如果支持则返回 true
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isAnnotationPresent(SourceModel.class);
    }

    /**
     * 对模型进行注解解析，
     * 获得 {@link SourceObject} 对应的对象，
     * 基于需要下载的原始数据对象进行适配，
     * 将属性通过反射重新设置。
     *
     * @param model   模型
     * @param context {@link DownloadContext}
     * @return 创建的 {@link Source}
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

    /**
     * 新建一个 {@link ReflectionTemplate}。
     *
     * @param clazz 模型类
     * @return 新建的 {@link ReflectionTemplate}
     */
    protected ReflectionTemplate newTemplate(Class<?> clazz) {
        return new ReflectionTemplate(clazz);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
