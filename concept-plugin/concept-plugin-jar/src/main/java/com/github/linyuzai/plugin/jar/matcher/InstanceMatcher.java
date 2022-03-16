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
import java.util.Map;
import java.util.stream.Collectors;

@DependOnResolvers(JarInstancePluginResolver.class)
public abstract class InstanceMatcher<T> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Type type, Annotation[] annotations) {
        Metadata metadata = getMetadata(type);
        if (metadata == null) {
            return false;
        }
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
        Map<String, ?> instances = context.get(JarPlugin.INSTANCES);
        Map<String, ?> map = filterByClass(instances, target);
        return setMatchedValue(context, metadata, map, "instance");
    }

    public Map<String, ?> filterByClass(Map<String, ?> instances, Class<?> target) {
        return instances.entrySet()
                .stream()
                .filter(it -> target.isInstance(it.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
