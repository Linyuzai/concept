package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;

/**
 * 插件自动重新加载事件
 */
@Getter
public class PluginAutoReloadEvent extends PluginAutoEvent {

    private final String path;

    public PluginAutoReloadEvent(Plugin plugin, String path) {
        super(plugin);
        this.path = path;
    }
}
