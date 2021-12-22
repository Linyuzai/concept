package com.github.linyuzai.download.core.source.reflection;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionSourceFactory implements SourceFactory {

    private final Map<Class<?>, ReflectionCache> reflectionCaches = new ConcurrentHashMap<>();

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source != null && source.getClass().isAnnotationPresent(SourceModel.class);
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        ReflectionCache cache = reflectionCaches.computeIfAbsent(source.getClass(), this::reflect);
        Object reflect;
        try {
            reflect = cache.reflect(SourceObject.class, source);
        } catch (ReflectiveOperationException e) {
            throw new DownloadException(e);
        }
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        SourceFactory factory = adapter.getFactory(reflect, context);
        return new ReflectionSource(cache, source, factory.create(reflect, context));
    }

    private ReflectionCache reflect(Class<?> clazz) {
        ReflectionCache cache = new ReflectionCache();
        List<Field> fields = new ArrayList<>();
        List<Method> methods = new ArrayList<>();
        Class<?> cls = clazz;
        while (cls != null) {
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
            cls = cls.getSuperclass();
        }
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(SourceReflection.class)) {
                    int count = method.getParameterCount();
                    if (count == 0) {
                        Method exist = cache.getMethodMap().get(annotationType);
                        if (exist == null) {
                            cache.getMethodMap().put(annotationType, method);
                        } else {
                            throw new DownloadException(annotationType.getName()
                                    + " is already defined on " + exist);
                        }
                    } else {
                        throw new DownloadException("Method cannot have parameters: " + method);
                    }
                }
            }
        }

        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(SourceReflection.class)) {
                    if (cache.getMethodMap().containsKey(annotationType)) {
                        //ignore
                    }
                    Field exist = cache.getFieldMap().get(annotationType);
                    if (exist == null) {
                        cache.getFieldMap().put(annotationType, field);
                    } else {
                        throw new DownloadException(annotationType.getName()
                                + " is already defined on " + exist);
                    }
                }
            }
        }
        return cache;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
