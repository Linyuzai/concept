package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.AllArgsConstructor;

/**
 * 插件 {@link Plugin} 匹配器
 */
@AllArgsConstructor
public class PluginObjectMatcher implements PluginMatcher {

    /**
     * 插件类
     */
    private final Class<?> clazz;

    public PluginObjectMatcher() {
        this(Plugin.class);
    }

    /**
     * 通过 {@link Class#isInstance(Object)} 匹配
     *
     * @param context 上下文 {@link PluginContext}
     * @return 匹配到的插件 {@link Plugin}
     */
    @Override
    public Object match(PluginContext context) {
        Plugin plugin = context.getPlugin();
        if (clazz.isInstance(plugin)) {
            return plugin;
        }
        return null;
    }
}
