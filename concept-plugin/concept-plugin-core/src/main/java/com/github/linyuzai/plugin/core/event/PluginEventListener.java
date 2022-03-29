package com.github.linyuzai.plugin.core.event;

/**
 * 插件事件 {@link PluginEvent} 监听器
 */
public interface PluginEventListener {

    /**
     * 事件监听回调
     *
     * @param event 事件
     */
    void onEvent(Object event);
}
