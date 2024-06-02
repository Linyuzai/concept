package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件解析事件
 */
@Getter
public class PluginResolvedEvent extends PluginContextEvent {

    /**
     * 插件解析器
     */
    private final PluginResolver resolver;

    /**
     * 依赖的 key
     */
    private final Object inboundKey;

    /**
     * 解析的 key
     */
    private final Object outboundKey;

    public PluginResolvedEvent(PluginContext context,
                               PluginResolver resolver,
                               Object inboundKey,
                               Object outboundKey) {
        super(context);
        this.resolver = resolver;
        this.inboundKey = inboundKey;
        this.outboundKey = outboundKey;
    }
}
