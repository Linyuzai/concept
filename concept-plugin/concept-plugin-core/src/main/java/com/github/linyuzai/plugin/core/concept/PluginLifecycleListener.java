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
        } else if (event instanceof PluginCreateErrorEvent ||
                event instanceof PluginPrepareErrorEvent ||
                event instanceof PluginLoadErrorEvent ||
                event instanceof PluginUnloadErrorEvent) {
            onError(((PluginErrorEvent) event).getDefinition(),
                    ((PluginErrorEvent) event).getError());
        }
    }

    /**
     * 创建
     */
    default void onCreate(Plugin plugin) {

    }

    /**
     * 准备
     */
    default void onPrepare(Plugin plugin) {

    }

    /**
     * 加载
     */
    default void onLoaded(Plugin plugin) {

    }

    /**
     * 卸载
     */
    default void onUnloaded(Plugin plugin) {

    }

    /**
     * 异常
     */
    default void onError(PluginDefinition definition, Throwable e) {

    }
}
