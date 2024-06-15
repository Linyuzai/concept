package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;

/**
 * 插件自动加载事件
 */
@Getter
public class PluginAutoLoadEvent extends PluginAutoEvent {

    private final String path;

    public PluginAutoLoadEvent(Plugin plugin, String path) {
        super(plugin);
        this.path = path;
    }
}
