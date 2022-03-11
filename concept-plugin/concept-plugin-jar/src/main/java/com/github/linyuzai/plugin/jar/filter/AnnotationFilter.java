package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
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
public class AnnotationFilter implements PluginFilter {

    private Collection<? extends Class<? extends Annotation>> annotationClasses;

    public AnnotationFilter(Class<? extends Annotation>... annotationClasses) {
        this(Arrays.asList(annotationClasses));
    }

    @Override
    public void filter(PluginContext context) {
        Collection<Class<?>> classes = context.get(JarPlugin.CLASSES);
        List<Class<?>> filteredClasses = classes.stream()
                .filter(this::hasAnnotation)
                .collect(Collectors.toList());
        context.set(JarPlugin.CLASSES, filteredClasses);
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
