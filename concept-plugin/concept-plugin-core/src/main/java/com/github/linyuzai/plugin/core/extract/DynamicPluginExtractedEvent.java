package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class DynamicPluginExtractedEvent extends PluginExtractedEvent {

    private final Method method;

    private final Object object;

    public DynamicPluginExtractedEvent(PluginContext context,
                                       PluginExtractor extractor,
                                       Object extracted,
                                       Method method,
                                       Object object) {
        super(context, extractor, extracted);
        this.method = method;
        this.object = object;
    }
}
