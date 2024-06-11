package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件准备事件
 */
public class PluginPreparedEvent extends PluginContextEvent {

    public PluginPreparedEvent(PluginContext context) {
        super(context);
    }
}
