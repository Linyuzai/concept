package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;

import java.lang.reflect.Type;

public abstract class GenericTypePluginMatcher<T> extends AbstractPluginMatcher<T> {

    private final Type matchingType = getMatchingType();

    @Override
    public boolean tryMatch(PluginContext context) {
        return tryMatch(context, matchingType);
    }

    public abstract boolean tryMatch(PluginContext context, Type type);

    public Type getMatchingType() {
        return getClass().getGenericSuperclass();
    }
}
