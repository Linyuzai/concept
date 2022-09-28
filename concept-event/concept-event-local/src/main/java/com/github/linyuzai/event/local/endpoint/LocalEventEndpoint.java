package com.github.linyuzai.event.local.endpoint;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.local.publisher.LocalEventPublisherImpl;
import com.github.linyuzai.event.local.subscriber.LocalEventSubscriberImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 本地事件端点
 */
@Getter
public class LocalEventEndpoint extends AbstractEventEndpoint {

    private final Collection<ListenerContainer> containers = new CopyOnWriteArrayList<>();

    public LocalEventEndpoint(@NonNull String name, @NonNull EventEngine engine) {
        super(name, engine);
    }

    @Override
    public void defaultPublish(Object event, EventContext context) {
        new LocalEventPublisherImpl().publish(event, this, context);
    }

    @Override
    public Subscription defaultSubscribe(EventListener listener, EventContext context) {
        return new LocalEventSubscriberImpl().subscribe(listener, this, context);
    }

    @Getter
    @AllArgsConstructor
    public static class ListenerContainer {

        private final LocalEventEndpoint endpoint;

        private final EventListener listener;

        private final EventContext context;

        public void consume(Object event) {
            listener.onEvent(event, endpoint, context);
        }

        public void remove() {
            endpoint.getContainers().remove(this);
        }
    }
}
