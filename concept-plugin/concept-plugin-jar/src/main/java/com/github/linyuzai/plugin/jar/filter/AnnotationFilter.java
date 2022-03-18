package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassPluginResolver.class)
public class AnnotationFilter extends AbstractPluginFilter<Map<String, Class<?>>> {

    private Collection<Class<? extends Annotation>> annotationClasses;

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
        return JarPlugin.CLASSES;
    }

    public boolean hasAnnotation(Class<?> clazz) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }
}
