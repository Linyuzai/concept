package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.matcher.GenericTypePluginMatcher;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.lang.reflect.Type;

public abstract class JarPluginMatcher extends GenericTypePluginMatcher<JarPlugin> {

    @Override
    public boolean tryMatch(PluginContext context, Type type) {
        return context.getPlugin() instanceof JarPlugin;
    }

    @Override
    public JarPlugin getMatched(PluginContext context) {
        return context.getPlugin();
    }

    @Override
    public Class<JarPlugin> getMatchingClass() {
        return JarPlugin.class;
    }
}
