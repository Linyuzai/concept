package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件格式化器抽象类
 *
 * @param <T> 格式化前的类型
 * @param <R> 格式化后的类型
 */
public abstract class AbstractPluginFormatter<T, R> implements PluginFormatter {

    @SuppressWarnings("unchecked")
    @Override
    public Object format(Object source, PluginContext context) {
        T original = (T) source;
        R formatted = doFormat(original, context);
        PluginFormattedEvent event = new PluginFormattedEvent(context, this, original, formatted);
        context.getConcept().getEventPublisher().publish(event);
        return formatted;
    }

    /**
     * 格式化
     */
    public abstract R doFormat(T source, PluginContext context);
}
