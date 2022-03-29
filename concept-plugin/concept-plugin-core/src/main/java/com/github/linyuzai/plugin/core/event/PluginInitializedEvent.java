package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件初始化事件
 */
public class PluginInitializedEvent extends AbstractPluginEvent {

    public PluginInitializedEvent(Plugin plugin) {
        super(plugin);
    }
}
