package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类上注解过滤器
 */
@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassResolver.class)
public class AnnotationFilter extends AbstractPluginFilter<Map<String, Class<?>>> {

    /**
     * 注解类
     */
    private final Collection<Class<? extends Annotation>> annotationClasses;

    @SafeVarargs
    public AnnotationFilter(Class<? extends Annotation>... annotationClasses) {
        this(Arrays.asList(annotationClasses));
    }

    @Override
    public Map<String, Class<?>> doFilter(Map<String, Class<?>> plugins) {
        return plugins.entrySet().stream()
                .filter(it -> filterWithNegation(hasAnnotation(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS;
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
