package com.github.linyuzai.plugin.core.format;

/**
 * {@link PluginFormatter} 的抽象类
 *
 * @param <T> 格式化前的类型
 * @param <R> 格式化后的类型
 */
public abstract class AbstractPluginFormatter<T, R> implements PluginFormatter {

    /**
     * 格式化
     *
     * @param source 被格式化的对象
     * @return 格式化后的对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object format(Object source) {
        return doFormat((T) source);
    }

    /**
     * 基于泛型的格式化
     *
     * @param source 被格式化的对象
     * @return 格式化后的对象
     */
    public abstract R doFormat(T source);
}
