package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@AllArgsConstructor
public class PluginObjectMatcher extends GenericTypePluginMatcher<Plugin> {

    private final Class<?> clazz;

    public PluginObjectMatcher() {
        this(Plugin.class);
    }

    @Override
    public boolean tryMatch(PluginContext context, Type type, Annotation[] annotations) {
        return type instanceof Class && ((Class<?>) type).isInstance(context.getPlugin());
    }

    @Override
    public boolean isMatched(PluginContext context) {
        return clazz.isInstance(context.getPlugin());
    }

    @Override
    public Plugin getMatched(PluginContext context) {
        return context.getPlugin();
    }
}
