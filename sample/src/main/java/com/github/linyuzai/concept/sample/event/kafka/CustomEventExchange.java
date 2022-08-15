package com.github.linyuzai.concept.sample.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import org.springframework.stereotype.Component;

import java.util.Collection;

//@Component
public class CustomEventExchange implements EventExchange {

    @Override
    public Collection<? extends EventEndpoint> exchange(Collection<? extends EventEngine> engines, EventContext context) {
        //自定义筛选
        return null;
    }
}
