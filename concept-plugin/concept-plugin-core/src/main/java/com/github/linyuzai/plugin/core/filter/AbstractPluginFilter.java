package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

public abstract class AbstractPluginFilter<T> implements PluginFilter {

    @Getter
    protected boolean negate;

    @Override
    public PluginFilter negate() {
        this.negate = !this.negate;
        return this;
    }

    @Override
    public void filter(PluginContext context) {
        Object key = getKey();
        T t = doFilter(context.get(key));
        context.set(key, t);
    }

    public boolean filterWithNegation(boolean filter) {
        return negate != filter;
    }

    public abstract Object getKey();

    public abstract T doFilter(T plugins);

}
