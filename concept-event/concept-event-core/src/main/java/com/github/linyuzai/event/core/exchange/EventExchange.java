package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.util.Collection;
import java.util.stream.Collectors;

public interface EventExchange {

    EventExchange ALL = concept -> concept.getEngines()
            .stream()
            .flatMap(it -> it.getEndpoints().stream())
            .collect(Collectors.toList());

    Collection<EventEndpoint> exchange(EventConcept concept);
}
