package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

public class PluginInitializedEvent extends AbstractPluginEvent {

    public PluginInitializedEvent(Plugin plugin) {
        super(plugin);
    }
}
