package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

@Getter
public class PluginFormattedEvent extends PluginContextEvent {

    private final PluginFormatter formatter;

    private final Object original;

    private final Object formatted;

    public PluginFormattedEvent(PluginContext context,
                                PluginFormatter formatter,
                                Object original,
                                Object formatted) {
        super(context);
        this.formatter = formatter;
        this.original = original;
        this.formatted = formatted;
    }
}
