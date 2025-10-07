package com.github.linyuzai.plugin.core.listener;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginListener {

    default void onLoad(Plugin plugin, PluginContext context) {

    }

    /**
     * 插件卸载
     */
    default void onUnload(Plugin plugin) {

    }
}
