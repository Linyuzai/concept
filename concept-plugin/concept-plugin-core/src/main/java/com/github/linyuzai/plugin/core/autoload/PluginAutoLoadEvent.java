package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件自动加载事件
 */
public class PluginAutoLoadEvent extends PluginAutoEvent {

    public PluginAutoLoadEvent(Plugin plugin) {
        super(plugin);
    }
}
