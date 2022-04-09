package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.event.AbstractPluginEvent;

/**
 * 插件自动事件
 */
public abstract class PluginAutoEvent extends AbstractPluginEvent {

    public PluginAutoEvent(Plugin plugin) {
        super(plugin);
    }
}
