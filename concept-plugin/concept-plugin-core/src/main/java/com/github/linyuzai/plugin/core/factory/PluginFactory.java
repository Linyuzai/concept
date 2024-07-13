package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;

/**
 * 插件工厂
 */
public interface PluginFactory {

    /**
     * 创建插件
     */
    Plugin create(Object source, PluginContext context, PluginConcept concept);

    /**
     * 获得插件配置工厂
     */
    PluginMetadataFactory getMetadataFactory();
}
