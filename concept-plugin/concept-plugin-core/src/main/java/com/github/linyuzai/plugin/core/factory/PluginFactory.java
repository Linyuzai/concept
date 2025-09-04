package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import org.jetbrains.annotations.Nullable;

/**
 * 插件工厂
 */
public interface PluginFactory {

    /**
     * 创建插件
     */
    @Nullable
    Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context);
}
