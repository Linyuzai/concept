package com.github.linyuzai.router.core.event;

import java.util.Arrays;
import java.util.Collection;

/**
 * 路由事件发布器
 */
public interface RouterEventPublisher {

    /**
     * 发布路由事件
     *
     * @param event 路由事件
     */
    void publish(Object event);

    /**
     * 注册路由事件监听器
     *
     * @param listeners 路由事件监听器
     */
    default void register(RouterEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    /**
     * 注册路由事件监听器
     *
     * @param listeners 路由事件监听器
     */
    void register(Collection<? extends RouterEventListener> listeners);
}
