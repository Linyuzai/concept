package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;

/**
 * 插件工厂
 */
public interface PluginFactory {

    /**
     * 创建插件 {@link Plugin}
     *
     * @param source       插件源
     * @param context 插件上下文
     * @return 插件 {@link Plugin}
     */
    Plugin create(Object source, PluginContext context);

    PluginMetadataFactory getMetadataFactory();
}
