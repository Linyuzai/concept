package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件树工厂
 */
public interface PluginTreeFactory {

    /**
     * 创建插件树
     */
    PluginTree create(Plugin plugin, PluginContext context, PluginConcept concept);
}
