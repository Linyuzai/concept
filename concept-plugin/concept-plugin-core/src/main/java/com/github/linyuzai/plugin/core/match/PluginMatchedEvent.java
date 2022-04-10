package com.github.linyuzai.plugin.core.match;

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
     * 原始对象
     */
    private final Object original;

    /**
     * 匹配到的对象
     */
    private final Object matched;

    public PluginMatchedEvent(PluginContext context,
                              PluginMatcher matcher,
                              Object original,
                              Object matched) {
        super(context);
        this.matcher = matcher;
        this.original = original;
        this.matched = matched;
    }
}
