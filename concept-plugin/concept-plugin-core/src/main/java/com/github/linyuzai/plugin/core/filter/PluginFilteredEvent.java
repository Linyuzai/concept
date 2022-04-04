package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

@Getter
public class PluginFilteredEvent extends PluginContextEvent {

    private final PluginFilter filter;

    private final Object original;

    private final Object filtered;

    public PluginFilteredEvent(PluginContext context,
                               PluginFilter filter,
                               Object original,
                               Object filtered) {
        super(context);
        this.filter = filter;
        this.original = original;
        this.filtered = filtered;
    }
}
