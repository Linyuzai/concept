package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.publisher.EventPublisherGroup;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultEventConcept implements EventConcept {

    private final Map<String, EventPublisherGroup> publisherGroupMap;

    public DefaultEventConcept(List<EventPublisherGroup> groups) {
        this.publisherGroupMap = groups.stream()
                .collect(Collectors.toConcurrentMap(EventPublisherGroup::getId, Function.identity()));
    }

    @Override
    public void publish(Object event) {

    }

    @Override
    public void publish(Object event, String... types) {

    }

    @Override
    public void publish(Object event, String type, String... names) {

    }
}
