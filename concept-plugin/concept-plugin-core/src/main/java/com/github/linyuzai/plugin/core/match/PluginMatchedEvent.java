package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

@Getter
public class PluginMatchedEvent extends PluginContextEvent {

    private final PluginMatcher matcher;

    private final Object key;

    private final Object original;

    private final Object matched;

    public PluginMatchedEvent(PluginContext context,
                              PluginMatcher matcher,
                              Object key,
                              Object original,
                              Object matched) {
        super(context);
        this.matcher = matcher;
        this.key = key;
        this.original = original;
        this.matched = matched;
    }
}
