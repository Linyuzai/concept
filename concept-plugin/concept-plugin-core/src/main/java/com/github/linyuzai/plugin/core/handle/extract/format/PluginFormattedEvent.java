package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件格式化事件
 */
@Getter
public class PluginFormattedEvent extends PluginContextEvent {

    /**
     * 插件格式器
     */
    private final PluginFormatter formatter;

    /**
     * 未格式化的内容
     */
    private final Object original;

    /**
     * 格式化后的内容
     */
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
