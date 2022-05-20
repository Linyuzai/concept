package com.github.linyuzai.connection.loadbalance.core.event;

import java.util.Arrays;
import java.util.Collection;

/**
 * 事件发布者
 */
public interface ConnectionEventPublisher {

    /**
     * 发布事件
     *
     * @param event 事件
     */
    void publish(Object event);

    /**
     * 注册事件监听器
     *
     * @param listeners 事件监听器
     */
    default void register(ConnectionEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    /**
     * 注册事件监听器
     *
     * @param listeners 事件监听器
     */
    void register(Collection<? extends ConnectionEventListener> listeners);
}
