package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件格式化事件
 */
@Getter
public class PluginFormattedEvent extends PluginContextEvent {

    /**
     * 插件格式器
     */
    private final PluginFormatter formatter;

    /**
     * 原始对象
     */
    private final Object original;

    /**
     * 格式化后的对象
     */
    private final Object formatted;

    public PluginFormattedEvent(PluginContext context,
                                PluginFormatter formatter,
                                Object original,
                                Object formatted) {
        super(context);
        this.formatter = formatter;
        this.original = original;
        this.formatted = formatted;
    }
}
