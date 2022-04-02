package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;

/**
 * 插件上下文 {@link PluginContext} 匹配器
 */
@AllArgsConstructor
public class PluginContextMatcher implements PluginMatcher {

    /**
     * 上下文类型
     */
    private final Class<?> clazz;

    public PluginContextMatcher() {
        this(PluginContext.class);
    }

    @Override
    public Object match(PluginContext context) {
        if (clazz.isInstance(context)) {
            return context;
        }
        return null;
    }
}
