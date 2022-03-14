package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class AbstractPluginMatcher<T> implements PluginMatcher {

    @Override
    public void match(PluginContext context) {
        if (tryMatch(context)) {
            onMatched(getMatched(context));
        }
    }

    public T getMatched(PluginContext context) {
        return context.get(this);
    }

    public abstract boolean tryMatch(PluginContext context);

    public abstract void onMatched(T plugin);
}
