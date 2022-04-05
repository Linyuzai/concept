package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件过滤事件
 */
@Getter
public class PluginFilteredEvent extends PluginContextEvent {

    /**
     * 插件过滤器
     */
    private final PluginFilter filter;

    /**
     * 原始对象
     */
    private final Object original;

    /**
     * 过滤后对象
     */
    private final Object filtered;

    public PluginFilteredEvent(PluginContext context,
                               PluginFilter filter,
                               Object original,
                               Object filtered) {
        super(context);
        this.filter = filter;
        this.original = original;
        this.filtered = filtered;
    }
}
