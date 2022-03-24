package com.github.linyuzai.plugin.core.format;

public abstract class AbstractPluginFormatter<T, R> implements PluginFormatter {

    @SuppressWarnings("unchecked")
    @Override
    public Object format(Object source) {
        return doFormat((T) source);
    }

    public abstract R doFormat(T source);
}
