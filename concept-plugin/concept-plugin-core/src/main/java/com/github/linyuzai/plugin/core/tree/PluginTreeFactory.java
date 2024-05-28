package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

public interface PluginTreeFactory {

    PluginTree create(Plugin plugin, PluginConcept concept);
}
