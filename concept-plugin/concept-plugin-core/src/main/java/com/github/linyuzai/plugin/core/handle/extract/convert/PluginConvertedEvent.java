package com.github.linyuzai.plugin.core.handle.extract.convert;

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
     * 未转换的内容
     */
    private final Object inbound;

    /**
     * 转换后的内容
     */
    private final Object outbound;

    public PluginConvertedEvent(PluginContext context,
                                PluginConvertor convertor,
                                Object inbound,
                                Object outbound) {
        super(context);
        this.convertor = convertor;
        this.inbound = inbound;
        this.outbound = outbound;
    }
}
