package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@AllArgsConstructor
public class PluginContextMatcher extends GenericTypePluginMatcher<PluginContext> {

    private final Class<?> clazz;

    public PluginContextMatcher() {
        this(PluginContext.class);
    }

    @Override
    public boolean tryMatch(PluginContext context, Type type, Annotation[] annotations) {
        return type instanceof Class && ((Class<?>) type).isInstance(context);
    }

    @Override
    public boolean isMatched(PluginContext context) {
        return clazz.isInstance(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PluginContext getMatched(PluginContext context) {
        return context;
    }

}
