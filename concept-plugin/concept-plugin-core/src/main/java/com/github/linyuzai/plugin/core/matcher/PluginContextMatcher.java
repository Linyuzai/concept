package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class PluginContextMatcher extends AbstractPluginMatcher<PluginContext> {

    @Override
    public boolean tryMatch(PluginContext context) {
        return true;
    }

    @Override
    public PluginContext getMatched(PluginContext context) {
        return context;
    }
}
