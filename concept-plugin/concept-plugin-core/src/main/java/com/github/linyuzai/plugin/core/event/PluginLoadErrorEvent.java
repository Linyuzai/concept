package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;

/**
 * 插件加载事件
 */
@Getter
public class PluginLoadErrorEvent extends AbstractPluginEvent implements PluginErrorEvent {

    private final Throwable error;

    public PluginLoadErrorEvent(Plugin plugin, Throwable error) {
        super(plugin);
        this.error = error;
    }
}
