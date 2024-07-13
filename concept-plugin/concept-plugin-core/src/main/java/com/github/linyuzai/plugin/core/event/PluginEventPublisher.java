package com.github.linyuzai.plugin.core.event;

import java.util.Arrays;
import java.util.Collection;

/**
 * 插件事件发布器
 */
public interface PluginEventPublisher {

    /**
     * 发布事件
     */
    void publish(Object event);

    /**
     * 注册事件监听器
     */
    default void register(PluginEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    /**
     * 注册事件监听器
     */
    void register(Collection<? extends PluginEventListener> listeners);

    /**
     * 注销事件监听器
     */
    default void unregister(PluginEventListener... listeners) {
        unregister(Arrays.asList(listeners));
    }

    /**
     * 注销事件监听器
     */
    void unregister(Collection<? extends PluginEventListener> listeners);


}
