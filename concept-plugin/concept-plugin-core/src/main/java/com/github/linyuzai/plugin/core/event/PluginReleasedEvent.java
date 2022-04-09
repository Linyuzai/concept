package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件释放事件
 */
public class PluginReleasedEvent extends AbstractPluginEvent {

    public PluginReleasedEvent(Plugin plugin) {
        super(plugin);
    }
}
