package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@DependOnResolvers(PropertiesPluginResolver.class)
public abstract class PropertiesMatcher<T> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Metadata metadata, Annotation[] annotations) {
        Type target = metadata.getTarget();
        if (target instanceof Class) {
            Class<?> clazz = (Class<?>) target;
            return setMatchedValueWithProperties(context, metadata, clazz);
        } else if (target instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) target).getRawType();
            Class<?> toClass = toClass(rawType);
            if (toClass != null) {
                return setMatchedValueWithProperties(context, metadata, toClass);
            }
        } else if (target instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) target).getUpperBounds();
            if (upperBounds.length > 0) {
                Class<?> toClass = toClass(upperBounds[0]);
                if (toClass != null) {
                    return setMatchedValueWithProperties(context, metadata, toClass);
                }
            }
        }
        return false;
    }

    public boolean setMatchedValueWithProperties(PluginContext context, Metadata metadata, Class<?> target) {
        Map<String, Properties> propertiesMap = context.get(Plugin.PROPERTIES);
        Map<String, Object> contents;
        if (Properties.class == target) {
            contents = new LinkedHashMap<>(propertiesMap);
        } else if (Map.class.isAssignableFrom(target)) {
            contents = new LinkedHashMap<>();
            for (Map.Entry<String, Properties> entry : propertiesMap.entrySet()) {
                Map<String, String> map = newMap(target);
                Properties properties = entry.getValue();
                for (String propertyName : properties.stringPropertyNames()) {
                    map.put(propertyName, properties.getProperty(propertyName));
                }
                contents.put(entry.getKey(), map);
            }
        } else {
            return false;
        }
        return setMatchedValue(context, metadata, contents, target, "properties");
    }
}
