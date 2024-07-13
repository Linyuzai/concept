package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.event.*;

/**
 * 插件生命周期监听
 */
public interface PluginLifecycleListener extends PluginEventListener {

    @Override
    default void onEvent(Object event) {
        if (event instanceof PluginCreatedEvent) {
            onCreate(((PluginCreatedEvent) event).getPlugin());
        } else if (event instanceof PluginPreparedEvent) {
            onPrepare(((PluginPreparedEvent) event).getPlugin());
        } else if (event instanceof PluginLoadedEvent) {
            onLoaded(((PluginLoadedEvent) event).getPlugin());
        } else if (event instanceof PluginUnloadedEvent) {
            onUnloaded(((PluginUnloadedEvent) event).getPlugin());
        }
    }

    /**
     * 创建
     */
    void onCreate(Plugin plugin);

    /**
     * 准备
     */
    void onPrepare(Plugin plugin);

    /**
     * 加载
     */
    void onLoaded(Plugin plugin);

    /**
     * 卸载
     */
    void onUnloaded(Plugin plugin);
}
