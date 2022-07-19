package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public interface EventConcept {

    EventOperator event();

    EventOperator event(Type type);

    EventOperator event(Object event);

    EventExchange getExchange();

    void setExchange(EventExchange exchange);

    EventEncoder getEncoder();

    void setEncoder(EventEncoder encoder);

    EventDecoder getDecoder();

    void setDecoder(EventDecoder decoder);

    EventErrorHandler getErrorHandler();

    void setErrorHandler(EventErrorHandler errorHandler);

    Collection<EventEngine> getEngines();

    default void add(EventEngine... engines) {
        add(Arrays.asList(engines));
    }

    void add(Collection<? extends EventEngine> engines);
}
