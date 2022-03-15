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
        Map<String, Class<?>> classes = context.get(JarPlugin.CLASSES);
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            if (Collection.class.isAssignableFrom(clazz)) {
                Collection<Class<?>> collection = newCollection(clazz);
                collection.addAll(classes.values());
                context.set(this, collection);
                return true;
            } else if (Map.class.isAssignableFrom(clazz)) {
                Map<String, Class<?>> map = newMap(clazz);
                map.putAll(classes);
                context.set(this, map);
                return true;
            } else {
                return filterOneClass(context, classes, clazz);
            }
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (rawType instanceof Class) {
                Class<?> rawClass = (Class<?>) rawType;
                if (Class.class.isAssignableFrom(rawClass)) {
                    Type actualTypeArgument = actualTypeArguments[0];
                    if (actualTypeArgument instanceof Class) {
                        return filterOneClass(context, classes, (Class<?>) actualTypeArgument);
                    } else if (actualTypeArgument instanceof WildcardType){
                        WildcardType wildcardType = (WildcardType) actualTypeArgument;
                        Type[] upperBounds = wildcardType.getUpperBounds();
                        if (upperBounds.length > 0) {
                            Type upperBound = upperBounds[0];
                            if (upperBound instanceof Class) {
                                return filterOneClass(context,classes, (Class<?>) upperBound);
                            }
                        }
                        //TODO ? super xxx 好像没有必要
                    }
                } else if (Collection.class.isAssignableFrom(rawClass)) {

                } else if (Map.class.isAssignableFrom(rawClass)) {

                }
            }
        }
        return false;
    }

    public boolean filterOneClass(PluginContext context, Map<String, Class<?>> classes, Class<?> clazz) {
        List<Class<?>> list = classes.values()
                .stream()
                .filter(clazz::isAssignableFrom)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            return false;
        }
        if (list.size() > 1) {
            throw new PluginException("More than one class found: " + list);
        }
        context.set(this, list.get(0));
        return true;
    }
}
