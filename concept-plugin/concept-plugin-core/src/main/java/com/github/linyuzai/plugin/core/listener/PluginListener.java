package com.github.linyuzai.plugin.core.listener;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

/**
 * 插件监听器
 */
public interface PluginListener {

    /**
     * 插件加载
     *
     * @param plugin  插件
     * @param context 插件上下文
     */
    default void onLoad(Plugin plugin, PluginContext context) {

    }

    /**
     * 插件卸载
     *
     * @param plugin 插件
     */
    default void onUnload(Plugin plugin) {

    }
}
