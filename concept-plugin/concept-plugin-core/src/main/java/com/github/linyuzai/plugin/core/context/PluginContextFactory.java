package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * {@link PluginContext} 工厂
 */
public interface PluginContextFactory {

    /**
     * 创建上下文 {@link PluginContext}
     *
     * @param plugin  插件 {@link Plugin}
     * @param concept {@link PluginConcept}
     * @return 上下文 {@link PluginContext}
     */
    PluginContext create(Plugin plugin, PluginConcept concept);
}
