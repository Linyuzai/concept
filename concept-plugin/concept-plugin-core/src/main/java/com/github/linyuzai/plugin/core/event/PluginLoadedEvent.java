package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件加载事件
 */
public class PluginLoadedEvent extends PluginContextEvent {

    public PluginLoadedEvent(PluginContext context) {
        super(context);
    }
}
