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
public class EngineExchange implements EventExchange {

    private Collection<String> types;

    public EngineExchange(String... types) {
        this(Arrays.asList(types));
    }

    public EngineExchange(Collection<String> types) {
        this.types = new HashSet<>(types);
    }

    @Override
    public Collection<EventPublishEngine> exchangeEngines(Collection<EventPublishEngine> engines) {
        return engines.stream().filter(it -> types.contains(it.getName())).collect(Collectors.toList());
    }

    @Override
    public Collection<EventPublishEndpoint> exchangeEndpoints(Collection<EventPublishEndpoint> endpoints) {
        return endpoints;
    }
}
