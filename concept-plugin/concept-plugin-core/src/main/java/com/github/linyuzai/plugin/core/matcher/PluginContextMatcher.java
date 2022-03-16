package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class PluginContextMatcher<T extends PluginContext> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Type type, Annotation[] annotations) {
        return type instanceof Class && ((Class<?>) type).isInstance(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getMatched(PluginContext context) {
        return (T) context;
    }
}
