package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

public class PluginCreatedEvent extends AbstractPluginEvent {

    public PluginCreatedEvent(Plugin plugin) {
        super(plugin);
    }
}
