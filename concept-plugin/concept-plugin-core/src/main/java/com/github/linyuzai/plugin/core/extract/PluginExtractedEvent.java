package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

@Getter
public class PluginExtractedEvent extends PluginContextEvent {

    private final PluginExtractor extractor;
    private final Object extracted;

    public PluginExtractedEvent(PluginContext context, PluginExtractor extractor, Object extracted) {
        super(context);
        this.extractor = extractor;
        this.extracted = extracted;
    }
}
