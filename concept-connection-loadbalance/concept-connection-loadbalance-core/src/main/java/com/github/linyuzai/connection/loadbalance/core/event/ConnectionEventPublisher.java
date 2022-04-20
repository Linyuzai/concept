package com.github.linyuzai.connection.loadbalance.core.event;

import java.util.Arrays;
import java.util.Collection;

public interface ConnectionEventPublisher {

    void publish(Object event);

    default void register(ConnectionEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    void register(Collection<? extends ConnectionEventListener> listeners);
}
