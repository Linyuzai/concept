package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件匹配事件
 */
@Getter
public class PluginMatchedEvent extends PluginContextEvent {

    /**
     * 插件匹配器
     */
    private final PluginMatcher matcher;

    /**
     * 依赖的 key
     */
    private final Object inboundKey;

    /**
     * 匹配到的内容
     */
    private final Object outbound;

    public PluginMatchedEvent(PluginContext context,
                              PluginMatcher matcher,
                              Object inboundKey,
                              Object outbound) {
        super(context);
        this.matcher = matcher;
        this.inboundKey = inboundKey;
        this.outbound = outbound;
    }
}
