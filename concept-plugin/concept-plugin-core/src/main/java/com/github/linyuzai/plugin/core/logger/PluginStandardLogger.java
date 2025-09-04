package com.github.linyuzai.plugin.core.logger;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginLifecycleListener;

/**
 * 插件标准日志
 */
public class PluginStandardLogger implements PluginLifecycleListener {

    @Override
    public void onCreate(Plugin plugin) {

    }

    @Override
    public void onPrepare(Plugin plugin) {

    }

    @Override
    public void onLoaded(Plugin plugin) {
        plugin.getConcept().getLogger().info("Loaded " + plugin);
    }

    @Override
    public void onUnloaded(Plugin plugin) {
        plugin.getConcept().getLogger().info("Unloaded " + plugin);
    }
}
