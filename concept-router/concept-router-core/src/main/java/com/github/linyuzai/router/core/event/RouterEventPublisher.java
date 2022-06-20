package com.github.linyuzai.router.core.event;

import java.util.Arrays;
import java.util.Collection;

public interface RouterEventPublisher {

    void publish(Object event);

    default void register(RouterEventListener... listeners) {
        register(Arrays.asList(listeners));
    }

    void register(Collection<? extends RouterEventListener> listeners);
}
