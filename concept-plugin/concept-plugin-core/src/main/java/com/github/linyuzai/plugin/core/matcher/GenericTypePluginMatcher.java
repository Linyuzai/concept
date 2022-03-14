package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;

import java.lang.reflect.Type;

public abstract class GenericTypePluginMatcher<T> extends AbstractPluginMatcher<T> {

    private final Class<T> matchingClass = getMatchingClass();

    @Override
    public boolean tryMatch(PluginContext context) {
        return tryMatch(context, matchingClass);
    }

    public abstract boolean tryMatch(PluginContext context, Type type);

    public abstract Class<T> getMatchingClass();

    public boolean matchClass(Class<?> clazz) {
        return matchingClass.isAssignableFrom(clazz);
    }

    public boolean matchInstance(Object instance) {
        return matchingClass.isInstance(instance);
    }
}
