package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

public class DefaultPluginTreeFactory implements PluginTreeFactory {

    @Override
    public PluginTree create(Plugin plugin, PluginConcept concept) {
        return new DefaultPluginTree(plugin);
    }
}
