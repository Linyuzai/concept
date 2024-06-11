package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件释放事件
 */
public class PluginReleasedEvent extends PluginContextEvent {

    public PluginReleasedEvent(PluginContext context) {
        super(context);
    }
}
