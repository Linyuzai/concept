package com.github.linyuzai.event.core.publisher;

public interface EventPublisher {

    EventPublisherGroup getGroup();

    void setGroup(EventPublisherGroup group);

    void publish(Object event);
}
