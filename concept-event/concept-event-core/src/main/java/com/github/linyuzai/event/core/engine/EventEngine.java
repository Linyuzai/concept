package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.concept.EventOperator;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.util.Arrays;
import java.util.Collection;

public interface EventEngine extends EventOperator.ObjectConfig {

    String getName();

    Collection<EventEndpoint> getEndpoints();

    default void addEndpoints(EventEndpoint... endpoints) {
        addEndpoints(Arrays.asList(endpoints));
    }

    void addEndpoints(Collection<? extends EventEndpoint> endpoints);

    default void removeEndpoints(String... endpoints) {
        removeEndpoints(Arrays.asList(endpoints));
    }

    void removeEndpoints(Collection<String> endpoints);
}
