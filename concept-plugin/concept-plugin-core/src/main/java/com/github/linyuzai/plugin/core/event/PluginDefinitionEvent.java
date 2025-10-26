package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;

/**
 * 插件定义事件
 */
public interface PluginDefinitionEvent {

    /**
     * 获得插件定义
     */
    PluginDefinition getDefinition();
}
