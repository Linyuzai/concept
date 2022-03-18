package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PluginContextMatcher implements PluginMatcher {

    private final Class<?> clazz;

    public PluginContextMatcher() {
        this(PluginContext.class);
    }

    @Override
    public Object match(PluginContext context) {
        if (clazz.isInstance(context)) {
            return context;
        }
        return null;
    }
}
