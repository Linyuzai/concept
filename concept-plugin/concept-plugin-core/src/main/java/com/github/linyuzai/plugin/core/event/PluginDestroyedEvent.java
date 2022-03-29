package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件销毁事件
 */
public class PluginDestroyedEvent extends AbstractPluginEvent {

    public PluginDestroyedEvent(Plugin plugin) {
        super(plugin);
    }
}
