package com.github.linyuzai.plugin.core.conflict;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.event.AbstractPluginEvent;
import lombok.Getter;

@Getter
public class PluginConflictedEvent extends AbstractPluginEvent {

    private final Plugin newPlugin;

    private final Plugin finalPlugin;

    public PluginConflictedEvent(Plugin plugin, Plugin newPlugin, Plugin finalPlugin) {
        super(plugin);
        this.newPlugin = newPlugin;
        this.finalPlugin = finalPlugin;
    }
}
