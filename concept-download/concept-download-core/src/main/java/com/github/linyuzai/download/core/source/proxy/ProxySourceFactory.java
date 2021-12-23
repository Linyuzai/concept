package com.github.linyuzai.download.core.source.proxy;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProxySourceFactory implements SourceFactory {

    private final Map<Class<?>, ReflectionTemplate> reflectionTemplateMap = new ConcurrentHashMap<>();

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isAnnotationPresent(SourceModel.class);
    }

    @Override
    public Source create(Object model, DownloadContext context) {
        ReflectionTemplate template = reflectionTemplateMap.computeIfAbsent(model.getClass(), ReflectionTemplate::new);
        Object reflect = template.reflect(SourceObject.class, model);
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        SourceFactory factory = adapter.getFactory(reflect, context);
        Source sourceToProxy = factory.create(reflect, context);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(sourceToProxy.getClass());
        enhancer.setCallback(new ProxySourceMethodInterceptor(model, template));
        Source proxy = (Source) enhancer.create();
        copySource(sourceToProxy, proxy);
        return proxy;
    }

    public void copySource(Source src, Source proxy) {
        try {
            Class<?> clazz = src.getClass();
            while (clazz != null) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(proxy, field.get(src));
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Throwable e) {
            throw new DownloadException(e);
        }
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
