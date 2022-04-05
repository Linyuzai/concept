package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件提取事件
 */
@Getter
public class PluginExtractedEvent extends PluginContextEvent {

    /**
     * 插件提取器
     */
    private final PluginExtractor extractor;

    /**
     * 提取到的插件对象
     */
    private final Object extracted;

    public PluginExtractedEvent(PluginContext context, PluginExtractor extractor, Object extracted) {
        super(context);
        this.extractor = extractor;
        this.extracted = extracted;
    }
}
