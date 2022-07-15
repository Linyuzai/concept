package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.engine.EventPublishEngine;
import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
@Setter
public class EndpointExchange implements EventExchange {

    private EngineExchange exchange;

    private Collection<String> endpoints;

    public EndpointExchange(String exchange, String... endpoints) {
        this(exchange, Arrays.asList(endpoints));
    }

    public EndpointExchange(String exchange, Collection<String> endpoints) {
        this.exchange = new EngineExchange(exchange);
        this.endpoints = new HashSet<>(endpoints);
    }

    @Override
    public Collection<EventPublishEngine> exchangeEngines(Collection<EventPublishEngine> engines) {
        return exchange.exchangeEngines(engines);
    }

    @Override
    public Collection<EventPublishEndpoint> exchangeEndpoints(Collection<EventPublishEndpoint> endpoints) {
        return endpoints.stream()
                .filter(it -> this.endpoints.contains(it.getName()))
                .collect(Collectors.toList());
    }
}
