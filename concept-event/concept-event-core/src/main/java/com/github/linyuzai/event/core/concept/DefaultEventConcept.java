package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.context.EventContextFactory;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.AccessLevel;
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

    protected final Map<String, EventEngine> engineMap = new ConcurrentHashMap<>();

    private EventContextFactory contextFactory;

    private EventExchange exchange;

    private EventEncoder encoder;

    private EventDecoder decoder;

    private EventErrorHandler errorHandler;

    @Override
    public EventOperator event() {
        return new EventOperatorImpl();
    }

    @Override
    public EventOperator event(Object event) {
        return new EventOperatorImpl(event);
    }

    protected void publishWithContext(Object event, EventContext context) {
        EventExchange exchange = applyExchange(context);
        EventPublisher publisher = context.get(EventPublisher.class);
        Collection<EventEndpoint> endpoints = exchange.exchange(this);
        for (EventEndpoint endpoint : endpoints) {
            EventContext prepare = prepareContext(context, endpoint);
            prepare.put(EventPublisher.class, usePublisher(endpoint, publisher));
            endpoint.publish(event, prepare);
        }
    }

    protected void subscribeWithContext(EventContext context) {
        EventExchange exchange = applyExchange(context);
        EventSubscriber subscriber = context.get(EventSubscriber.class);
        Collection<EventEndpoint> endpoints = exchange.exchange(this);
        for (EventEndpoint endpoint : endpoints) {
            EventContext prepare = prepareContext(context, endpoint);
            prepare.put(EventSubscriber.class, useSubscriber(endpoint, subscriber));
            endpoint.subscribe(prepare);
        }
    }

    protected EventExchange applyExchange(EventContext context) {
        EventExchange exchange = context.get(EventExchange.class);
        EventExchange exchangeToUse = useExchange(exchange);
        context.put(EventExchange.class, exchangeToUse);
        return exchangeToUse;
    }

    protected EventContext prepareContext(EventContext context, EventEndpoint endpoint) {
        EventContext duplicate = context.duplicate();
        duplicate.put(EventConcept.class, this);
        EventEncoder encoder = duplicate.get(EventEncoder.class);
        duplicate.put(EventEncoder.class, useEncoder(endpoint, encoder));
        EventDecoder decoder = duplicate.get(EventDecoder.class);
        duplicate.put(EventDecoder.class, useDecoder(endpoint, decoder));
        EventErrorHandler errorHandler = duplicate.get(EventErrorHandler.class);
        duplicate.put(EventErrorHandler.class, useErrorHandler(endpoint, errorHandler));
        return duplicate;
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

    protected EventEncoder useEncoder(EventEndpoint endpoint, EventEncoder encoder) {
        if (encoder != null) {
            return encoder;
        }
        if (endpoint.getEncoder() != null) {
            return endpoint.getEncoder();
        }
        if (endpoint.getEngine().getEncoder() != null) {
            return endpoint.getEngine().getEncoder();
        }
        return this.encoder;
    }

    protected EventDecoder useDecoder(EventEndpoint endpoint, EventDecoder decoder) {
        if (decoder != null) {
            return decoder;
        }
        if (endpoint.getDecoder() != null) {
            return endpoint.getDecoder();
        }
        if (endpoint.getEngine().getDecoder() != null) {
            return endpoint.getEngine().getDecoder();
        }
        return this.decoder;
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

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    protected class EventOperatorImpl implements EventOperator {

        protected Object event;

        protected EventExchange exchange;

        protected EventEncoder encoder;

        protected EventDecoder decoder;

        protected EventErrorHandler errorHandler;

        protected EventOperatorImpl(Object event) {
            this.event = event;
        }

        public EventOperator exchange(EventExchange exchange) {
            this.exchange = exchange;
            return this;
        }

        public EventOperator encoder(EventEncoder encoder) {
            this.encoder = encoder;
            return this;
        }

        public EventOperator decoder(EventDecoder decoder) {
            this.decoder = decoder;
            return this;
        }

        public EventOperator error(EventErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        @Override
        public void publish() {
            publish(null);
        }

        public void publish(EventPublisher publisher) {
            EventContext context = buildContext();
            context.put(EventPublisher.class, publisher);
            publishWithContext(event, context);
        }

        @Override
        public void subscribe() {
            subscribe(null);
        }

        public void subscribe(EventSubscriber subscriber) {
            EventContext context = buildContext();
            context.put(EventSubscriber.class, subscriber);
            subscribeWithContext(context);
        }

        protected EventContext buildContext() {
            EventContext context = contextFactory.create();
            context.put(EventExchange.class, exchange);
            context.put(EventEncoder.class, encoder);
            context.put(EventDecoder.class, decoder);
            context.put(EventErrorHandler.class, errorHandler);
            return context;
        }
    }
}
