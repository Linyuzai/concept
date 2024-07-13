package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * 插件上下文默认实现
 */
public class DefaultPluginContextFactory implements PluginContextFactory {

    @Override
    public PluginContext create(PluginConcept concept) {
        return new DefaultPluginContext();
    }
}
