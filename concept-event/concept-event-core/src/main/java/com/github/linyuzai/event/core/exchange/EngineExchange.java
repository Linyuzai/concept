package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 引擎交换机
 * <p>
 * 指定对应事件引擎的所有端点
 */
@Getter
@Setter
public class EngineExchange implements EventExchange {

    private Collection<String> engines;

    public EngineExchange(String... engines) {
        this(Arrays.asList(engines));
    }

    public EngineExchange(Collection<String> engines) {
        this.engines = new HashSet<>(engines);
    }

    @Override
    public Collection<EventEndpoint> exchange(Collection<EventEngine> engines, EventContext context) {
        return engines.stream()
                .filter(it -> this.engines.contains(it.getName()))
                .flatMap(it -> it.getEndpoints().stream())
                .collect(Collectors.toList());
    }
}
