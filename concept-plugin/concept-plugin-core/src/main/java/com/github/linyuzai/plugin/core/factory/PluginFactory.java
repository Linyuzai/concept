package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;

/**
 * 插件工厂
 */
public interface PluginFactory {

    /**
     * 创建插件
     */
    Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context);
}
