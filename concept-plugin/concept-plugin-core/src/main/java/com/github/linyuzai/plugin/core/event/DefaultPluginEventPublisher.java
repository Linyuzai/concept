package com.github.linyuzai.plugin.core.event;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@link PluginEventPublisher} 默认实现
 */
public class DefaultPluginEventPublisher implements PluginEventPublisher {

    /**
     * 所有的监听器
     */
    private final Collection<PluginEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * 发布事件。
     * 遍历所有的事件监听器。
     *
     * @param event 事件
     */
    @Override
    public void publish(Object event) {
        for (PluginEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    @Override
    public void register(Collection<? extends PluginEventListener> listeners) {
        this.listeners.addAll(listeners);
    }

    @Override
    public void unregister(Collection<? extends PluginEventListener> listeners) {
        this.listeners.removeAll(listeners);
    }

    /**
     * 获得所有事件监听器 {@link PluginEventListener} 的不可变集合
     *
     * @return {@link PluginEventListener} 的不可变集合
     */
    public Collection<PluginEventListener> getEventListeners() {
        return Collections.unmodifiableCollection(listeners);
    }
}
