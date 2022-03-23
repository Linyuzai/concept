package com.github.linyuzai.plugin.core.convert;

public abstract class AbstractPluginConvertor<T, R> implements PluginConvertor {

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object source) {
        return doConvert((T) source);
    }

    public abstract R doConvert(T source);
}
