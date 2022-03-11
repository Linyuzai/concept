package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

@Getter
public class PluginContextCreatedEvent extends AbstractPluginEvent {

    private final PluginContext context;

    public PluginContextCreatedEvent(Plugin plugin, PluginContext context) {
        super(plugin);
        this.context = context;
    }
}
