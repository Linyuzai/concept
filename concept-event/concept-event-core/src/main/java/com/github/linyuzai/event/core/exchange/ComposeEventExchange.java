package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 组合事件交换机
 * <p>
 * 可以组合多个事件交换机
 * <p>
 * 已对端点去重
 */
@Getter
@AllArgsConstructor
public class ComposeEventExchange implements EventExchange {

    /**
     * 多个事件交换机
     */
    private Collection<EventExchange> exchanges;

    public ComposeEventExchange(EventExchange... exchanges) {
        this(Arrays.asList(exchanges));
    }

    @Override
    public Collection<? extends EventEndpoint> exchange(Collection<? extends EventEngine> engines, EventContext context) {
        return exchanges.stream()
                .flatMap(it -> it.exchange(engines, context).stream())
                .collect(Collectors.toSet());
    }
}
