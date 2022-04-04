package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * {@link PluginConvertor} 抽象类。
 *
 * @param <T> 转换前的数据类型
 * @param <R> 转换后的数据类型
 */
public abstract class AbstractPluginConvertor<T, R> implements PluginConvertor {

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object source, PluginContext context) {
        T original = (T) source;
        R converted = doConvert(original);
        context.publish(new PluginConvertedEvent(context, this, original, converted));
        return converted;
    }

    public abstract R doConvert(T source);
}
