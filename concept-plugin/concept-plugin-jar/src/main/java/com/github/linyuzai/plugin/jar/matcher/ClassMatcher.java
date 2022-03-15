package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.matcher.GenericTypePluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassPluginResolver;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.stream.Collectors;

@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher<T> extends GenericTypePluginMatcher<T> {

    @SneakyThrows
    @Override
    public boolean tryMatch(PluginContext context, Type type) {
        Metadata metadata = getMetadata(type);
        if (metadata == null) {
            return false;
        }
        Type target = metadata.getTarget();
        if (target instanceof Class) {
            Class<?> clazz = (Class<?>) target;
            return setContextValue(context, metadata, clazz);
        } else if (target instanceof ParameterizedType) {
            return handleParameterizedType(context, metadata, (ParameterizedType) target);
        } else if (target instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) target).getUpperBounds();
            if (upperBounds.length > 0) {
                Type upperBound = upperBounds[0];
                if (upperBound instanceof Class) {
                    if (Class.class.isAssignableFrom((Class<?>) upperBound)) {
                        return setContextValue(context, metadata, Object.class);
                    }
                } else if (upperBound instanceof ParameterizedType) {
                    return handleParameterizedType(context, metadata, (ParameterizedType) upperBound);
                }
            }
        }
        return false;
    }

    public boolean handleParameterizedType(PluginContext context, Metadata metadata, ParameterizedType type) {
        Type rawType = type.getRawType();
        if (rawType instanceof Class) {
            if (Class.class.isAssignableFrom((Class<?>) rawType)) {
                Type[] arguments = type.getActualTypeArguments();
                Class<?> toClass = toClass(arguments[0]);
                if (toClass != null) {
                    return setContextValue(context, metadata, toClass);
                }
            }
        }
        return false;
    }

    public boolean setContextValue(PluginContext context, Metadata metadata, Class<?> target) {
        Map<String, Class<?>> classes = context.get(JarPlugin.CLASSES);
        Map<String, Class<?>> map = filterByClass(classes, target);
        if (map.isEmpty()) {
            return false;
        }
        if (metadata.isMap()) {
            metadata.getMap().putAll(map);
            context.set(this, metadata.getMap());
            return true;
        } else if (metadata.isList()) {
            metadata.getList().addAll(map.values());
            context.set(this, metadata.getList());
            return true;
        } else if (metadata.isSet()) {
            metadata.getSet().addAll(map.values());
            context.set(this, metadata.getSet());
            return true;
        } else if (metadata.isCollection()) {
            metadata.getCollection().addAll(map.values());
            context.set(this, metadata.getCollection());
            return true;
        } else {
            List<Class<?>> list = new ArrayList<>(map.values());
            if (list.size() > 1) {
                throw new PluginException("More than one class found: " + list);
            }
            context.set(this, list.get(0));
            return true;
        }
    }

    public Map<String, Class<?>> filterByClass(Map<String, Class<?>> classes, Class<?> target) {
        return classes.entrySet()
                .stream()
                .filter(it -> target.isAssignableFrom(it.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
