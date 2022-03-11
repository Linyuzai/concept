package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassPluginResolver.class)
public class AnnotationFilter extends AbstractPluginFilter<List<Class<?>>> {

    private Collection<? extends Class<? extends Annotation>> annotationClasses;

    @SafeVarargs
    public AnnotationFilter(Class<? extends Annotation>... annotationClasses) {
        this(Arrays.asList(annotationClasses));
    }

    @Override
    public List<Class<?>> doFilter(List<Class<?>> plugins) {
        return plugins.stream()
                .filter(this::hasAnnotation)
                .collect(Collectors.toList());
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASSES;
    }

    private boolean hasAnnotation(Class<?> clazz) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }
}
