package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件准备事件
 */
public class PluginPreparedEvent extends AbstractPluginEvent {

    public PluginPreparedEvent(Plugin plugin) {
        super(plugin);
    }
}
