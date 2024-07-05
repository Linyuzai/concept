package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
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
public class ClassAnnotationFilter extends AbstractPluginFilter<JarClass> {

    /**
     * 注解类
     */
    private final Collection<Class<? extends Annotation>> annotationClasses;

    @SafeVarargs
    public ClassAnnotationFilter(Class<? extends Annotation>... annotationClasses) {
        this(Arrays.asList(annotationClasses));
    }

    @Override
    public boolean doFilter(JarClass jarClass) {
        return hasAnnotation(jarClass.get());
    }

    @Override
    public Object getKey() {
        return Class.class;
    }

    /**
     * 类上是否有对应注解
     *
     * @param cls 类
     * @return 如果存在对应注解返回 true 否则返回 false
     */
    public boolean hasAnnotation(Class<?> cls) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (cls.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }
}
