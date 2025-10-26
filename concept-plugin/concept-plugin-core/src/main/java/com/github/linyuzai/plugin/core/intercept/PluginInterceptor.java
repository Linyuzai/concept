package com.github.linyuzai.plugin.core.intercept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;

/**
 * 调用顺序:
 * <p>
 * =>beforeCreatePlugin
 * <p>
 * ==>beforeCreateMetadata
 * <p>
 * ==>afterCreateMetadata
 * <p>
 * =>afterCreatePlugin
 */
public interface PluginInterceptor {

    /**
     * 插件创建前回调
     *
     * @param definition 插件定义
     * @param context    插件上下文
     */
    default void beforeCreatePlugin(PluginDefinition definition, PluginContext context) {

    }

    /**
     * 插件元数据创建前回调
     *
     * @param definition 插件定义
     * @param context    插件上下文
     */
    default void beforeCreateMetadata(PluginDefinition definition, PluginContext context) {

    }

    /**
     * 插件元数据创建后回调
     *
     * @param metadata   插件元数据
     * @param definition 插件定义
     * @param context    插件上下文
     */
    default void afterCreateMetadata(PluginMetadata metadata, PluginDefinition definition, PluginContext context) {

    }

    /**
     * 插件创建后回调
     *
     * @param plugin     插件
     * @param definition 插件定义
     * @param context    插件上下文
     */
    default void afterCreatePlugin(Plugin plugin, PluginDefinition definition, PluginContext context) {

    }
}
