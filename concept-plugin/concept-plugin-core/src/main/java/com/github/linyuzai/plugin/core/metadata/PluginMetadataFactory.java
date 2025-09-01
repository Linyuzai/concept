package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginMetadataFactory {

    /**
     * 创建插件配置
     */
    PluginMetadata create(PluginDefinition definition, PluginContext context);
}
