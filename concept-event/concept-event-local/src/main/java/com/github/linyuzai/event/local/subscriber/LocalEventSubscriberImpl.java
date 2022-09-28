package com.github.linyuzai.event.local.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpoint;

public class LocalEventSubscriberImpl extends LocalEventSubscriber {

    @Override
    public Subscription doSubscribe(EventListener listener, LocalEventEndpoint endpoint, EventContext context) {
        LocalEventEndpoint.ListenerContainer listenerContainer =
                new LocalEventEndpoint.ListenerContainer(endpoint, listener, context);
        endpoint.getContainers().add(listenerContainer);
        return new LocalSubscription(listenerContainer);
    }
}
