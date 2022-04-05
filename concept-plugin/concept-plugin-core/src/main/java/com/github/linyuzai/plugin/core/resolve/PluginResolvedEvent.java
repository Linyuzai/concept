package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginContextEvent;
import lombok.Getter;

/**
 * 插件解析事件
 */
@Getter
public class PluginResolvedEvent extends PluginContextEvent {

    /**
     * 插件解析器
     */
    private final PluginResolver resolver;

    /**
     * 依赖的 key
     */
    private final Object dependedKey;

    /**
     * 依赖的对象
     */
    private final Object depended;

    /**
     * 解析的 key
     */
    private final Object resolvedKey;

    /**
     * 解析后的对象
     */
    private final Object resolved;

    public PluginResolvedEvent(PluginContext context,
                               PluginResolver resolver,
                               Object resolvedKey,
                               Object resolved) {
        super(context);
        this.resolver = resolver;
        this.dependedKey = null;
        this.depended = null;
        this.resolvedKey = resolvedKey;
        this.resolved = resolved;
    }

    public PluginResolvedEvent(PluginContext context,
                               PluginResolver resolver,
                               Object dependedKey,
                               Object depended,
                               Object resolvedKey,
                               Object resolved) {
        super(context);
        this.resolver = resolver;
        this.dependedKey = dependedKey;
        this.depended = depended;
        this.resolvedKey = resolvedKey;
        this.resolved = resolved;
    }
}
