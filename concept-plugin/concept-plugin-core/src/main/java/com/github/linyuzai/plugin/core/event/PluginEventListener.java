package com.github.linyuzai.plugin.core.event;

/**
 * 插件事件监听器
 */
public interface PluginEventListener {

    /**
     * 事件监听回调
     */
    void onEvent(Object event);
}
