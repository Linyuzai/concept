package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.matcher.GenericTypePluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarInstancePluginResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@DependOnResolvers(JarInstancePluginResolver.class)
public abstract class InstanceMatcher<T> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Metadata metadata, Annotation[] annotations) {
        Type target = metadata.getTarget();
        if (target instanceof Class) {
            Class<?> clazz = (Class<?>) target;
            return setMatchedValueWithInstance(context, metadata, clazz);
        } else if (target instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) target).getRawType();
            Class<?> toClass = toClass(rawType);
            if (toClass != null) {
                return setMatchedValueWithInstance(context, metadata, toClass);
            }
        } else if (target instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) target).getUpperBounds();
            if (upperBounds.length > 0) {
                Class<?> toClass = toClass(upperBounds[0]);
                if (toClass != null) {
                    return setMatchedValueWithInstance(context, metadata, toClass);
                }
            }
        }
        return false;
    }

    public boolean setMatchedValueWithInstance(PluginContext context, Metadata metadata, Class<?> target) {
        Map<String, Object> instances = context.get(JarPlugin.INSTANCES);
        Map<String, Object> map = filterByClass(instances, target);
        return setMatchedValue(context, metadata, map, target, "instance");
    }

    public Map<String, Object> filterByClass(Map<String, Object> instances, Class<?> target) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : instances.entrySet()) {
            Object value = entry.getValue();
            if (target.isInstance(value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }
}
