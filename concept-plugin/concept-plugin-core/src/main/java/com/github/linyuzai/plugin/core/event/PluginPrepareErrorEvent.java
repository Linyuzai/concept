package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

/**
 * 插件准备事件
 */
@Getter
public class PluginPrepareErrorEvent extends PluginContextEvent implements PluginErrorEvent {

    private final Throwable error;

    public PluginPrepareErrorEvent(PluginContext context, Throwable error) {
        super(context);
        this.error = error;
    }
}
