package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

public class PluginUnloadedEvent extends AbstractPluginEvent {

    public PluginUnloadedEvent(Plugin plugin) {
        super(plugin);
    }
}
