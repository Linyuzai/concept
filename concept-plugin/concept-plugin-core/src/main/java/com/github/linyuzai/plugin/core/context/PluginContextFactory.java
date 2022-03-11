package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

public interface PluginContextFactory {

    PluginContext create(Plugin plugin, PluginConcept concept);
}
