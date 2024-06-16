package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.event.AbstractPluginEvent;
import lombok.Getter;

/**
 * 插件自动加载事件
 */
@Getter
public class PluginAutoLoadEvent extends AbstractPluginEvent implements PluginAutoEvent {

    private final String path;

    public PluginAutoLoadEvent(Plugin plugin, String path) {
        super(plugin);
        this.path = path;
    }
}
