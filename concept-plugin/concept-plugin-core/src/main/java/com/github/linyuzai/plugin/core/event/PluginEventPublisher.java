package com.github.linyuzai.plugin.core.event;

import java.util.Arrays;
import java.util.Collection;

/**
 * 插件事件发布者
 */
public interface PluginEventPublisher {

    /**
     * 发布事件
     *
     * @param event 事件
     */
    void publish(Object event);

    /**
     * 注册事件监听器 {@link PluginEventListener}
     *
     * @param listeners 事件监听器 {@link PluginEventListener}
     */
    default void register(PluginEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    /**
     * 注册事件监听器 {@link PluginEventListener}
     *
     * @param listeners 事件监听器 {@link PluginEventListener}
     */
    void register(Collection<? extends PluginEventListener> listeners);

    default void unregister(PluginEventListener... listeners) {
        unregister(Arrays.asList(listeners));
    }

    void unregister(Collection<? extends PluginEventListener> listeners);


}
