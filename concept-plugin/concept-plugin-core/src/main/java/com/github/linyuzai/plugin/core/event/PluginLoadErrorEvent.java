package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

/**
 * 插件加载事件
 */
@Getter
public class PluginLoadErrorEvent extends PluginContextEvent implements PluginErrorEvent {

    private final Throwable error;

    public PluginLoadErrorEvent(PluginContext context, Throwable error) {
        super(context);
        this.error = error;
    }
}
