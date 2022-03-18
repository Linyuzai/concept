package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@DependOnResolvers(PropertiesPluginResolver.class)
public abstract class PropertiesMatcher extends AbstractPluginMatcher<Properties> {

    protected Class<?> target;

    protected Function<Properties, Object> function;

    public PropertiesMatcher(Class<?> target, Annotation[] annotations) {
        this.target = target;
        if (target != Properties.class && Map.class.isAssignableFrom(target)) {
            this.function = properties -> {
                Map<String, String> newMap = ReflectionUtils.newMap(target);
                for (String propertyName : properties.stringPropertyNames()) {
                    newMap.put(propertyName, properties.getProperty(propertyName));
                }
                return newMap;
            };
        }
    }

    @Override
    public Object getKey() {
        return Plugin.PROPERTIES;
    }

    public Map<String, Object> filter(Map<String, Properties> propertiesMap) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Properties> entry : propertiesMap.entrySet()) {
            if (function == null) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                map.put(entry.getKey(), function.apply(entry.getValue()));
            }
        }
        return map;
    }

    @Getter
    public static class MapMatcher extends PropertiesMatcher implements MapConvertor {

        private final Class<?> mapClass;

        public MapMatcher(Class<?> mapClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.mapClass = mapClass;
        }
    }

    @Getter
    public static class ListMatcher extends PropertiesMatcher implements ListConvertor {

        private final Class<?> listClass;

        public ListMatcher(Class<?> listClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.listClass = listClass;
        }
    }

    @Getter
    public static class SetMatcher extends PropertiesMatcher implements SetConvertor {

        private final Class<?> setClass;

        public SetMatcher(Class<?> setClass, Class<?> target, Annotation[] annotations) {
            super(target, annotations);
            this.setClass = setClass;
        }
    }

    public static class ArrayMatcher extends PropertiesMatcher implements ArrayConvertor {

        public ArrayMatcher(Class<?> target, Annotation[] annotations) {
            super(target, annotations);
        }

        @Override
        public Class<?> getArrayClass() {
            return target;
        }
    }

    public static class ObjectMatcher extends PropertiesMatcher implements ObjectConvertor {

        public ObjectMatcher(Class<?> target, Annotation[] annotations) {
            super(target, annotations);
        }

        @Override
        public String getType() {
            return "properties";
        }
    }
}
