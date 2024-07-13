package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * 插件上下文工厂
 */
public interface PluginContextFactory {

    /**
     * 创建上下文
     */
    PluginContext create(PluginConcept concept);
}
