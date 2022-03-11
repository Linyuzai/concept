package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.matcher.AbstractPluginMatcher;
import com.github.linyuzai.plugin.jar.JarPlugin;

public abstract class JarPluginMatcher extends AbstractPluginMatcher<JarPlugin> {

    @Override
    public boolean ifMatch(PluginContext context) {
        return true;
    }

    @Override
    public JarPlugin getMatchedPlugin(PluginContext context) {
        return context.getPlugin();
    }
}
