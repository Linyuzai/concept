package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;

public interface PluginContextFactory {

    PluginContext create(Plugin plugin);
}
