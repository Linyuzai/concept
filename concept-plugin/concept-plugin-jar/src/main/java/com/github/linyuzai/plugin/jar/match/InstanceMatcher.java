package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarInstancePluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@DependOnResolvers(JarInstancePluginResolver.class)
public abstract class InstanceMatcher extends AbstractPluginMatcher<Object> {

    protected final Class<?> target;

    protected final Annotation[] annotations;

    @Override
    public Object getKey() {
        return JarPlugin.INSTANCE;
    }

    public Map<String, Object> filter(Map<String, Object> instances) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : instances.entrySet()) {
            Object value = entry.getValue();
            if (target.isInstance(value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Getter
    public static class MapMatcher extends InstanceMatcher implements MapConvertor {

        private final Class<?> mapClass;

        public MapMatcher(Class<?> mapClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.mapClass = mapClass;
        }
    }

    @Getter
    public static class ListMatcher extends InstanceMatcher implements ListConvertor {

        private final Class<?> listClass;

        public ListMatcher(Class<?> listClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.listClass = listClass;
        }
    }

    @Getter
    public static class SetMatcher extends InstanceMatcher implements SetConvertor {

        private final Class<?> setClass;

        public SetMatcher(Class<?> setClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.setClass = setClass;
        }
    }

    public static class ArrayMatcher extends InstanceMatcher implements ArrayConvertor {

        public ArrayMatcher(Class<?> target, Annotation[] annotations) {
            super(target, annotations);
        }

        @Override
        public Class<?> getArrayClass() {
            return target;
        }
    }

    public static class ObjectMatcher extends InstanceMatcher implements ObjectConvertor {

        public ObjectMatcher(Class<?> target, Annotation[] annotations) {
            super(target, annotations);
        }

        @Override
        public String getType() {
            return "instance";
        }
    }
}
