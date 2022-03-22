package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher extends AbstractJarPluginMatcher<Class<?>> {

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS;
    }

    public Map<String, Object> filter(Map<String, Class<?>> classes) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Class<?> value = entry.getValue();
            if (target.isAssignableFrom(value) && filterWithAnnotation(value)) {
                map.put(entry.getKey(), value);
            }
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
