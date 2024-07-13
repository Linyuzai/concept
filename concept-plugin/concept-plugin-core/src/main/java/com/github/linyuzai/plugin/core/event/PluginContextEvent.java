package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

/**
 * 附带上下文的插件事件
 */
@Getter
public abstract class PluginContextEvent extends AbstractPluginEvent {

    private final PluginContext context;

    public PluginContextEvent(PluginContext context) {
        super(context.getPlugin());
        this.context = context;
    }
}
