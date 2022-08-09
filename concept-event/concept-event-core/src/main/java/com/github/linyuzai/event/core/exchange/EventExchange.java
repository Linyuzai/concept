package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;

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
    EventExchange ALL = (engines, context) -> engines
            .stream()
            .flatMap(it -> it.getEndpoints().stream())
            .collect(Collectors.toList());

    Collection<? extends EventEndpoint> exchange(Collection<? extends EventEngine> engines, EventContext context);
}
