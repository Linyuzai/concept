package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.context.EventContextFactory;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class DefaultEventConcept implements EventConcept {

    private final Map<String, EventEngine> engineMap = new ConcurrentHashMap<>();

    private EventContextFactory contextFactory;

    private EventExchange exchange = EventExchange.ALL;

    private EventErrorHandler errorHandler;

    @Override
    public EventBuilder event() {
        return new EventBuilderImpl();
    }

    @Override
    public EventBuilder event(Object event) {
        return new EventBuilderImpl(event);
    }

    protected void publishWithContext(Object event, EventContext context) {
        EventExchange exchange = context.get(EventExchange.class);
        EventPublisher publisher = context.get(EventPublisher.class);
        Collection<EventEndpoint> endpoints = useExchange(exchange).exchange(this);
        for (EventEndpoint endpoint : endpoints) {
            endpoint.publish(event, usePublisher(endpoint, publisher));
        }
    }

    public void subscribe(EventSubscriber subscriber) {
        subscribe(exchange, subscriber);
    }
    
    public void subscribe(EventExchange exchange, EventSubscriber subscriber) {
        Collection<EventEndpoint> endpoints = useExchange(exchange).exchange(this);
        for (EventEndpoint endpoint : endpoints) {
            endpoint.subscribe(useSubscriber(endpoint, subscriber));
        }
    }

    @Override
    public Collection<EventEngine> getEngines() {
        return Collections.unmodifiableCollection(engineMap.values());
    }

    @Override
    public void add(Collection<? extends EventEngine> engines) {
        for (EventEngine engine : engines) {
            this.engineMap.put(engine.getName(), engine);
        }
    }

    protected EventExchange useExchange(EventExchange exchange) {
        if (exchange != null) {
            return exchange;
        }
        if (this.exchange != null) {
            return this.exchange;
        }
        return EventExchange.ALL;
    }

    protected EventPublisher usePublisher(EventEndpoint endpoint, EventPublisher publisher) {
        if (publisher != null) {
            return publisher;
        }
        if (endpoint.getPublisher() != null) {
            return endpoint.getPublisher();
        }
        if (endpoint.getEngine().getPublisher() != null) {
            return endpoint.getEngine().getPublisher();
        }
        return null;
    }

    protected EventSubscriber useSubscriber(EventEndpoint endpoint, EventSubscriber subscriber) {
        if (subscriber != null) {
            return subscriber;
        }
        if (endpoint.getSubscriber() != null) {
            return endpoint.getSubscriber();
        }
        if (endpoint.getEngine().getSubscriber() != null) {
            return endpoint.getEngine().getSubscriber();
        }
        return null;
    }

    @NoArgsConstructor
    public class EventBuilderImpl implements EventBuilder {

        private Object event;

        private EventExchange exchange;

        private EventErrorHandler errorHandler;

        public EventBuilderImpl(Object event) {
            this.event = event;
        }

        public EventBuilder exchange(EventExchange exchange) {
            this.exchange = exchange;
            return this;
        }

        public EventBuilder error(EventErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        @Override
        public void publish() {

        }

        public void publish(EventPublisher publisher) {
            EventContext context = contextFactory.create();
            //TODO
            publishWithContext(event, context);
        }

        @Override
        public void subscribe() {

        }

        public void subscribe(EventSubscriber subscriber) {
            DefaultEventConcept.this.subscribe(exchange, subscriber);
        }
    }
}
