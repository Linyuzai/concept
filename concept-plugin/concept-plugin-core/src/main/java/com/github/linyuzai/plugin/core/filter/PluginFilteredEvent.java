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
     * 依赖的 key
     */
    private final Object inboundKey;

    /**
     * 解析的 key
     */
    private final Object outboundKey;

    public PluginFilteredEvent(PluginContext context,
                               PluginFilter filter,
                               Object inboundKey,
                               Object outboundKey) {
        super(context);
        this.filter = filter;
        this.inboundKey = inboundKey;
        this.outboundKey = outboundKey;
    }
}
