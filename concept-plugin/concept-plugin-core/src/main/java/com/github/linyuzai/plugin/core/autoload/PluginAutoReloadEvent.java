package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件自动重新加载事件
 */
public class PluginAutoReloadEvent extends PluginAutoEvent {

    public PluginAutoReloadEvent(Plugin plugin) {
        super(plugin);
    }
}
