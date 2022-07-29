package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 端点交换机
 * <p>
 * 指定某个事件引擎的部分端点
 */
@Getter
@Setter
public class EndpointExchange implements EventExchange {

    private EngineExchange engine;

    private Collection<String> endpoints;

    public EndpointExchange(String engine, String... endpoints) {
        this(engine, Arrays.asList(endpoints));
    }

    public EndpointExchange(EngineExchange engine, String... endpoints) {
        this(engine, Arrays.asList(endpoints));
    }

    public EndpointExchange(String engine, Collection<String> endpoints) {
        this(new EngineExchange(engine), endpoints);
    }

    public EndpointExchange(EngineExchange engine, Collection<String> endpoints) {
        this.engine = engine;
        this.endpoints = new HashSet<>(endpoints);
    }

    @Override
    public Collection<EventEndpoint> exchange(EventConcept concept) {
        return engine.exchange(concept)
                .stream()
                .filter(it -> endpoints.contains(it.getName()))
                .collect(Collectors.toList());
    }
}
