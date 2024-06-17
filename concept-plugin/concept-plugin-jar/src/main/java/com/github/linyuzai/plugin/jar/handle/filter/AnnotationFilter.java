package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

/**
 * 类上注解过滤器
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(JarClassResolver.class)
public class AnnotationFilter extends AbstractPluginFilter<Class<?>> {

    /**
     * 注解类
     */
    private final Collection<Class<? extends Annotation>> annotationClasses;

    @SafeVarargs
    public AnnotationFilter(Class<? extends Annotation>... annotationClasses) {
        this(Arrays.asList(annotationClasses));
    }

    @Override
    public boolean doFilter(Class<?> clazz) {
        return hasAnnotation(clazz);
    }

    @Override
    public Object getKey() {
        return Class.class;
    }

    /**
     * 类上是否有对应注解
     *
     * @param clazz 类
     * @return 如果存在对应注解返回 true 否则返回 false
     */
    public boolean hasAnnotation(Class<?> clazz) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }
}
