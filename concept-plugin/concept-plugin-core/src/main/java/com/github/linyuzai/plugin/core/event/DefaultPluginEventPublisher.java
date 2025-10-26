package com.github.linyuzai.plugin.core.event;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 插件事件发布器默认实现
 */
public class DefaultPluginEventPublisher implements PluginEventPublisher {

    /**
     * 监听器
     */
    private final List<PluginEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 遍历所有的事件监听器发布事件
     */
    @Override
    public void publish(Object event) {
        for (PluginEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    /**
     * 注册事件监听器
     */
    @Override
    public void register(Collection<? extends PluginEventListener> listeners) {
        this.listeners.addAll(listeners);
    }

    /**
     * 注销事件监听器
     */
    @Override
    public void unregister(Collection<? extends PluginEventListener> listeners) {
        this.listeners.removeAll(listeners);
    }

    /**
     * 获得事件监听器列表
     */
    public List<PluginEventListener> getEventListeners() {
        return Collections.unmodifiableList(listeners);
    }
}
