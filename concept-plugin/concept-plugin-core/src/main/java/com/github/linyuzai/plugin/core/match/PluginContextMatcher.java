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

    /**
     * 通过 {@link Class#isInstance(Object)} 匹配
     *
     * @param context 上下文 {@link PluginContext}
     * @return 匹配到的上下文 {@link PluginContext}
     */
    @Override
    public Object match(PluginContext context) {
        if (clazz.isInstance(context)) {
            return context;
        }
        return null;
    }
}
