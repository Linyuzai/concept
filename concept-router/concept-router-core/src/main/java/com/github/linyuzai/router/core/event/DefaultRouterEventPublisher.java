package com.github.linyuzai.router.core.event;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 路由事件发布器的默认实现
 */
@NoArgsConstructor
public class DefaultRouterEventPublisher implements RouterEventPublisher {

    private final Collection<RouterEventListener> listeners = new CopyOnWriteArrayList<>();

    public DefaultRouterEventPublisher(Collection<? extends RouterEventListener> listeners) {
        register(listeners);
    }

    @Override
    public void publish(Object event) {
        listeners.forEach(it -> it.onEvent(event));
    }

    @Override
    public void register(Collection<? extends RouterEventListener> listeners) {
        this.listeners.addAll(listeners);
    }
}
