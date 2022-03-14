package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.lang.reflect.Type;

public abstract class PluginObjectMatcher<T extends Plugin> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Type type) {
        return type instanceof Class && ((Class<?>) type).isInstance(context.getPlugin());
    }

    @Override
    public T getMatched(PluginContext context) {
        return context.getPlugin();
    }
}
