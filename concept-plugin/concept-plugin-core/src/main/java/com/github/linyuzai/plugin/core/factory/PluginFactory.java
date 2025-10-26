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
     *
     * @param definition 插件定义
     * @param metadata   插件元数据
     * @param context    插件上下文
     * @return 插件
     */
    @Nullable
    Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context);
}
