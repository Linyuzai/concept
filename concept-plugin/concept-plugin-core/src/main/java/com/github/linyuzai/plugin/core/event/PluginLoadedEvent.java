package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件加载事件
 */
public class PluginLoadedEvent extends AbstractPluginEvent {

    public PluginLoadedEvent(Plugin plugin) {
        super(plugin);
    }
}
