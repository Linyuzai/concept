package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.filter.AnnotationFilter;
import com.github.linyuzai.plugin.jar.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;

import java.lang.annotation.Annotation;
import java.util.*;

@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher extends AbstractPluginMatcher {

    protected final Class<?> target;

    protected PackageFilter packageFilter;

    protected ClassFilter classFilter;

    protected AnnotationFilter annotationFilter;

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        this.target = target;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginPackage.class) {
                String[] packages = ((PluginPackage) annotation).value();
                if (packages.length > 0) {
                    packageFilter = new PackageFilter(packages);
                }
            } else if (annotation.annotationType() == PluginClassName.class) {

            } else if (annotation.annotationType() == PluginClass.class) {
                Class<?>[] classes = ((PluginClass) annotation).value();
                if (classes.length > 0) {
                    classFilter = new ClassFilter(classes);
                }
            } else if (annotation.annotationType() == PluginAnnotation.class) {
                Class<? extends Annotation>[] classes = ((PluginAnnotation) annotation).value();
                if (classes.length > 0) {
                    annotationFilter = new AnnotationFilter(classes);
                }
            }
        }
    }

    @Override
    public Object match(PluginContext context) {
        Map<String, Class<?>> classes = context.get(JarPlugin.CLASSES);
        Map<String, Class<?>> map = filter(classes);
        if (map.isEmpty()) {
            return null;
        }
        return convert(map);
    }

    public Map<String, Class<?>> filter(Map<String, Class<?>> classes) {
        Map<String, Class<?>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Class<?> value = entry.getValue();
            if (!target.isAssignableFrom(value)) {
                continue;
            }
            if (packageFilter != null && !packageFilter.matchPackages(value.getName())) {
                continue;
            }
            if (classFilter != null && !classFilter.matchClasses(value)) {
                continue;
            }
            if (annotationFilter != null && !annotationFilter.hasAnnotation(value)) {
                continue;
            }
            map.put(entry.getKey(), value);
        }
        return map;
    }

    public abstract Object convert(Map<String, Class<?>> map);
}
