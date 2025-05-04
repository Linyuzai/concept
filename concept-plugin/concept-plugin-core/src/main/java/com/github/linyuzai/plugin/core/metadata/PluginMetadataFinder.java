package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginMetadataFinder {

    /**
     * 创建插件配置
     */
    PluginMetadata find(Object source, PluginContext context);
}
