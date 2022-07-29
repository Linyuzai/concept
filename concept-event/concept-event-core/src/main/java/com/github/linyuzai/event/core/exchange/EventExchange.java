package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 事件交换机
 * <p>
 * 指定在发布的事件端点或订阅的事件端点
 */
public interface EventExchange {

    /**
     * 所有端点
     */
    EventExchange ALL = concept -> concept.getEngines()
            .stream()
            .flatMap(it -> it.getEndpoints().stream())
            .collect(Collectors.toList());

    Collection<EventEndpoint> exchange(EventConcept concept);
}
