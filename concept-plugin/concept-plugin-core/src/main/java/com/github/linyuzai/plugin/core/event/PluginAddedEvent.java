package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

public class PluginAddedEvent extends AbstractPluginEvent {

    public PluginAddedEvent(Plugin plugin) {
        super(plugin);
    }
}
