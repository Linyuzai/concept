package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件元数据工厂
 */
public interface PluginMetadataFactory {

    /**
     * 创建插件元数据
     */
    PluginMetadata create(PluginDefinition definition, PluginContext context);
}
