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

    default void beforeCreatePlugin(PluginDefinition definition, PluginContext context) {

    }

    default void beforeCreateMetadata(PluginDefinition definition, PluginContext context) {

    }

    default void afterCreateMetadata(PluginMetadata metadata, PluginDefinition definition, PluginContext context) {

    }

    default void afterCreatePlugin(Plugin plugin, PluginDefinition definition, PluginContext context) {

    }
}
