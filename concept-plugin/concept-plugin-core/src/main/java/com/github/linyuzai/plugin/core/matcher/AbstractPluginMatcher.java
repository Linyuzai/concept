package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class AbstractPluginMatcher<T> implements PluginMatcher {

    @Override
    public void match(PluginContext context) {
        if (ifMatch(context)) {
            onMatched(getMatchedPlugin(context));
        }
    }

    public T getMatchedPlugin(PluginContext context) {
        return context.get(this);
    }

    public abstract boolean ifMatch(PluginContext context);

    public abstract void onMatched(T plugin);
}
