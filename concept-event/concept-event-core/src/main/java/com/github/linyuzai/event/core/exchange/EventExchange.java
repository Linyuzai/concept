package com.github.linyuzai.event.core.exchange;

import com.github.linyuzai.event.core.engine.EventPublishEngine;
import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;

import java.util.Collection;

public interface EventExchange {

    EventExchange ALL = new EventExchange() {
        @Override
        public Collection<EventPublishEngine> exchangeEngines(Collection<EventPublishEngine> engines) {
            return engines;
        }

        @Override
        public Collection<EventPublishEndpoint> exchangeEndpoints(Collection<EventPublishEndpoint> endpoints) {
            return endpoints;
        }
    };

    Collection<EventPublishEngine> exchangeEngines(Collection<EventPublishEngine> engines);

    Collection<EventPublishEndpoint> exchangeEndpoints(Collection<EventPublishEndpoint> endpoints);
}
