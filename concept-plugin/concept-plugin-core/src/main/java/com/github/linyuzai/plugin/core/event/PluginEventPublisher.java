package com.github.linyuzai.plugin.core.event;

import java.util.Arrays;
import java.util.Collection;

public interface PluginEventPublisher {

    void publish(Object event);

    default void register(PluginEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    void register(Collection<? extends PluginEventListener> listeners);
}
