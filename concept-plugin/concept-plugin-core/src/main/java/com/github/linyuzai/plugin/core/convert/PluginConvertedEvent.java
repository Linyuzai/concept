package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件转换事件
 */
@Getter
public class PluginConvertedEvent extends PluginContextEvent {

    /**
     * 转换器
     */
    private final PluginConvertor convertor;

    /**
     * 原始对象
     */
    private final Object original;

    /**
     * 转换后对象
     */
    private final Object converted;

    public PluginConvertedEvent(PluginContext context,
                                PluginConvertor convertor,
                                Object original,
                                Object converted) {
        super(context);
        this.convertor = convertor;
        this.original = original;
        this.converted = converted;
    }
}
