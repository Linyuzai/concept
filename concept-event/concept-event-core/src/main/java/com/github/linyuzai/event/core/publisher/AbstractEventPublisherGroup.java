package com.github.linyuzai.event.core.publisher;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractEventPublisherGroup implements EventPublisherGroup {

    private final List<EventPublisher> publishers = new CopyOnWriteArrayList<>();

    @Override
    public List<EventPublisher> getEventPublishers() {
        return publishers;
    }

    @Override
    public void add(Collection<? extends EventPublisher> publishers) {
        for (EventPublisher publisher : publishers) {
            publisher.setGroup(this);
        }
        this.publishers.addAll(publishers);
    }
}
