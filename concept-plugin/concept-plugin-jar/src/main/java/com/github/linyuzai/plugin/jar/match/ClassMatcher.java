package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.filter.AnnotationFilter;
import com.github.linyuzai.plugin.jar.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.filter.ClassNameFilter;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher extends AbstractPluginMatcher<Class<?>> {

    protected final Class<?> target;

    protected PackageFilter packageFilter;

    protected ClassNameFilter classNameFilter;

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
                String[] classNames = ((PluginClassName) annotation).value();
                if (classNames.length > 0) {
                    classNameFilter = new ClassNameFilter(classNames);
                }
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
    public Object getKey() {
        return JarPlugin.CLASSES;
    }

    public Map<String, Object> filter(Map<String, Class<?>> classes) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Class<?> value = entry.getValue();
            if (!target.isAssignableFrom(value)) {
                continue;
            }
            if (packageFilter != null && !packageFilter.matchPackages(value.getName())) {
                continue;
            }
            if (classNameFilter != null && !classNameFilter.matchClassNames(value.getName())) {
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

    @Getter
    public static class MapMatcher extends ClassMatcher implements MapConvertor {

        private final Class<?> mapClass;

        public MapMatcher(Class<?> mapClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.mapClass = mapClass;
        }
    }

    @Getter
    public static class ListMatcher extends ClassMatcher implements ListConvertor {

        private final Class<?> listClass;

        public ListMatcher(Class<?> listClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.listClass = listClass;
        }
    }

    @Getter
    public static class SetMatcher extends ClassMatcher implements SetConvertor {

        private final Class<?> setClass;

        public SetMatcher(Class<?> setClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.setClass = setClass;
        }
    }

    public static class ArrayMatcher extends ClassMatcher implements ArrayConvertor {

        public ArrayMatcher(Class<?> target, Annotation[] annotations) {
            super(target, annotations);
        }

        @Override
        public Class<?> getArrayClass() {
            return target;
        }
    }

    public static class ObjectMatcher extends ClassMatcher implements ObjectConvertor {

        public ObjectMatcher(Class<?> target, Annotation[] annotations) {
            super(target, annotations);
        }

        @Override
        public String getType() {
            return "class";
        }
    }
}
