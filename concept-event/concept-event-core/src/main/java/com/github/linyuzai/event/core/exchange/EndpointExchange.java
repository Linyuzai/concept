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

    private Collection<String> names;

    public EndpointExchange(String type, String... names) {
        this(type, Arrays.asList(names));
    }

    public EndpointExchange(String type, Collection<String> names) {
        exchange = new EngineExchange(type);
        this.names = new HashSet<>(names);
    }

    @Override
    public Collection<EventPublishEngine> exchangeEngines(Collection<EventPublishEngine> engines) {
        return exchange.exchangeEngines(engines);
    }

    @Override
    public Collection<EventPublishEndpoint> exchangeEndpoints(Collection<EventPublishEndpoint> endpoints) {
        return endpoints.stream()
                .filter(it -> names.contains(it.getName()))
                .collect(Collectors.toList());
    }
}
