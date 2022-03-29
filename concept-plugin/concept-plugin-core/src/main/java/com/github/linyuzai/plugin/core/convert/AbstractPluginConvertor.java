package com.github.linyuzai.plugin.core.convert;

/**
 * {@link PluginConvertor} 抽象类。
 *
 * @param <T> 转换前的数据类型
 * @param <R> 转换后的数据类型
 */
public abstract class AbstractPluginConvertor<T, R> implements PluginConvertor {

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object source) {
        return doConvert((T) source);
    }

    public abstract R doConvert(T source);
}
