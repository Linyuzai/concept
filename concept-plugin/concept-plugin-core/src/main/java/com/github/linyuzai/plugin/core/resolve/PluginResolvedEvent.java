package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

@Getter
public class PluginResolvedEvent extends PluginContextEvent {

    private final PluginResolver resolver;

    private final Object dependedKey;

    private final Object depended;

    private final Object resolvedKey;

    private final Object resolved;

    public PluginResolvedEvent(PluginContext context,
                               PluginResolver resolver,
                               Object resolvedKey,
                               Object resolved) {
        super(context);
        this.resolver = resolver;
        this.dependedKey = null;
        this.depended = null;
        this.resolvedKey = resolvedKey;
        this.resolved = resolved;
    }

    public PluginResolvedEvent(PluginContext context,
                               PluginResolver resolver,
                               Object dependedKey,
                               Object depended,
                               Object resolvedKey,
                               Object resolved) {
        super(context);
        this.resolver = resolver;
        this.dependedKey = dependedKey;
        this.depended = depended;
        this.resolvedKey = resolvedKey;
        this.resolved = resolved;
    }
}
