package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * {@link PluginContextFactory} 默认实现
 */
public class DefaultPluginContextFactory implements PluginContextFactory {

    /**
     * 创建一个默认上下文 {@link DefaultPluginContext}
     *
     * @param plugin  插件 {@link Plugin}
     * @param concept {@link PluginConcept}
     * @return 上下文 {@link DefaultPluginContext}
     */
    @Override
    public PluginContext create(Plugin plugin, PluginConcept concept) {
        return new DefaultPluginContext(concept, plugin);
    }
}
