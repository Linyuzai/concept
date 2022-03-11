package com.github.linyuzai.plugin.core.event;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultPluginEventPublisher implements PluginEventPublisher {

    private final Collection<PluginEventListener> listeners = new ArrayList<>();

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
}
