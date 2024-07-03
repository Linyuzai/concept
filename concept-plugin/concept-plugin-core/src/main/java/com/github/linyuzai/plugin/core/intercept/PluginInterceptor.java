package com.github.linyuzai.plugin.core.intercept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

@Deprecated
public interface PluginInterceptor {

    void beforeLoaded(Plugin plugin, PluginContext context);

    void afterLoaded(Plugin plugin, PluginContext context);

    void beforeUnloaded(Plugin plugin, PluginContext context);

    void afterUnloaded(Plugin plugin, PluginContext context);
}
