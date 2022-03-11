package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

public class PluginRemovedEvent extends AbstractPluginEvent {

    public PluginRemovedEvent(Plugin plugin) {
        super(plugin);
    }
}
