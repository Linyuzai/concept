package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.SneakyThrows;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class GenericTypePluginMatcher<T> extends AbstractPluginMatcher<T> {

    private final Type matchingType = getMatchingType();

    @Override
    public boolean tryMatch(PluginContext context) {
        return tryMatch(context, matchingType);
    }

    public abstract boolean tryMatch(PluginContext context, Type type);

    public Type getMatchingType() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length == 1) {
                return types[0];
            }
        }
        throw new PluginException("U may need to try override this method");
    }

    @SneakyThrows
    public <E> Collection<E> newCollection(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Set.class.isAssignableFrom(clazz)) {
            if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                return new HashSet<>();
            } else {
                return (Collection<E>) clazz.newInstance();
            }
        } else {
            if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                return new ArrayList<>();
            } else {
                return (Collection<E>) clazz.newInstance();
            }
        }
    }

    @SneakyThrows
    public <E> Map<String, E> newMap(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new HashMap<>();
        } else {
            return (Map<String, E>) clazz.newInstance();
        }
    }
}
