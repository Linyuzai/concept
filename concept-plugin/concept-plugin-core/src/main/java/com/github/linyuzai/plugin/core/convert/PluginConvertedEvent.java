package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

@Getter
public class PluginConvertedEvent extends PluginContextEvent {

    private final PluginConvertor convertor;

    private final Object original;

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
