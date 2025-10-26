package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件处理器工厂，用于动态追加插件处理器
 */
public interface PluginHandlerFactory {

    /**
     * 创建插件处理器
     */
    PluginHandler create(Plugin plugin, PluginContext context, PluginConcept concept);
}
