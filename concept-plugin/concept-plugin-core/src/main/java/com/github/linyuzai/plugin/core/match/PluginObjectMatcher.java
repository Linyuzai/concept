package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PluginObjectMatcher implements PluginMatcher {

    private final Class<?> clazz;

    public PluginObjectMatcher() {
        this(Plugin.class);
    }

    @Override
    public Object match(PluginContext context) {
        Plugin plugin = context.getPlugin();
        if (clazz.isInstance(plugin)) {
            return plugin;
        }
        return null;
    }
}
