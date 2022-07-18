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

    private EventExchange exchange;

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
        EventExchange exchangeToUse = useExchange(exchange);
        context.put(EventExchange.class, exchangeToUse);
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        EventPublisher publisher = context.get(EventPublisher.class);
        Collection<EventEndpoint> endpoints = exchangeToUse.exchange(this);
        for (EventEndpoint endpoint : endpoints) {
            EventContext duplicate = context.duplicate();
            duplicate.put(EventErrorHandler.class, useErrorHandler(endpoint, errorHandler));
            duplicate.put(EventPublisher.class, usePublisher(endpoint, publisher));
            endpoint.publish(event, duplicate);
        }
    }


    protected void subscribeWithContext(EventContext context) {
        EventExchange exchange = context.get(EventExchange.class);
        EventExchange exchangeToUse = useExchange(exchange);
        context.put(EventExchange.class, exchangeToUse);
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        EventSubscriber subscriber = context.get(EventSubscriber.class);
        Collection<EventEndpoint> endpoints = exchangeToUse.exchange(this);
        for (EventEndpoint endpoint : endpoints) {
            EventContext duplicate = context.duplicate();
            duplicate.put(EventErrorHandler.class, useErrorHandler(endpoint, errorHandler));
            duplicate.put(EventSubscriber.class, useSubscriber(endpoint, subscriber));
            endpoint.subscribe(duplicate);
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

    protected EventErrorHandler useErrorHandler(EventEndpoint endpoint, EventErrorHandler errorHandler) {
        if (errorHandler != null) {
            return errorHandler;
        }
        if (endpoint.getErrorHandler() != null) {
            return endpoint.getErrorHandler();
        }
        if (endpoint.getEngine().getErrorHandler() != null) {
            return endpoint.getEngine().getErrorHandler();
        }
        return this.errorHandler;
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
    private class EventBuilderImpl implements EventBuilder {

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
            publish(null);
        }

        public void publish(EventPublisher publisher) {
            EventContext context = contextFactory.create();
            context.put(EventExchange.class, exchange);
            context.put(EventErrorHandler.class, errorHandler);
            context.put(EventPublisher.class, publisher);
            publishWithContext(event, context);
        }

        @Override
        public void subscribe() {
            subscribe(null);
        }

        public void subscribe(EventSubscriber subscriber) {
            EventContext context = contextFactory.create();
            context.put(EventExchange.class, exchange);
            context.put(EventErrorHandler.class, errorHandler);
            context.put(EventSubscriber.class, subscriber);
            subscribeWithContext(context);
        }
    }
}
