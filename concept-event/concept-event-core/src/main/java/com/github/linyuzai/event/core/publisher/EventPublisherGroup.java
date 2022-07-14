package com.github.linyuzai.event.core.publisher;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface EventPublisherGroup {

    String getId();

    List<EventPublisher> getEventPublishers();

    default void add(EventPublisher... publishers) {
        add(Arrays.asList(publishers));
    }

    void add(Collection<? extends EventPublisher> publishers);
}
