package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public interface EventConcept {

    void initialize();

    void destroy();

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

    default void addEngines(EventEngine... engines) {
        addEngines(Arrays.asList(engines));
    }

    void addEngines(Collection<? extends EventEngine> engines);

    default void removeEngines(String... engines) {
        removeEngines(Arrays.asList(engines));
    }

    void removeEngines(Collection<String> engines);

    Collection<EventConceptLifecycleListener> getLifecycleListeners();

    default void addLifecycleListeners(EventConceptLifecycleListener... lifecycleListeners) {
        addLifecycleListeners(Arrays.asList(lifecycleListeners));
    }

    void addLifecycleListeners(Collection<? extends EventConceptLifecycleListener> lifecycleListeners);

    default void removeLifecycleListeners(EventConceptLifecycleListener... lifecycleListeners) {
        removeLifecycleListeners(Arrays.asList(lifecycleListeners));
    }

    void removeLifecycleListeners(Collection<? extends EventConceptLifecycleListener> lifecycleListeners);
}
