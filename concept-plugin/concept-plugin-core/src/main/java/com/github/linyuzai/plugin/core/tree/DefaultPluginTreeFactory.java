package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件树工厂默认实现
 */
public class DefaultPluginTreeFactory implements PluginTreeFactory {

    @Override
    public PluginTree create(Plugin plugin, PluginContext context, PluginConcept concept) {
        return new DefaultPluginTree(plugin);
    }
}
