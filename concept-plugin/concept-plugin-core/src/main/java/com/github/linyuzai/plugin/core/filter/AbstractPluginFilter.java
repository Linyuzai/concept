package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class AbstractPluginFilter<T> implements PluginFilter {

    @Override
    public void filter(PluginContext context) {
        Object key = getKey();
        T t = doFilter(context.get(key));
        context.set(key, t);
    }

    public abstract T doFilter(T plugins);

    public abstract Object getKey();
}
