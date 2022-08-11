package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;

import java.util.Collection;
import java.util.stream.Collectors;

public class Business1EventExchange implements EventExchange {

    @Override
    public Collection<? extends EventEndpoint> exchange(Collection<? extends EventEngine> engines, EventContext context) {
        return engines.stream()
                .flatMap(it -> it.getEndpoints().stream())
                .filter(it -> it.getName().equals("kafka1") ||
                        it.getName().equals("rabbitmq2"))
                .collect(Collectors.toList());
    }
}
