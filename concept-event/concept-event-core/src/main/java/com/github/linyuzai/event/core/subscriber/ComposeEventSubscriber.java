package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.EventListener;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class ComposeEventSubscriber implements EventSubscriber {

    private final Collection<EventSubscriber> subscribers;

    public ComposeEventSubscriber(EventSubscriber... subscribers) {
        this(Arrays.asList(subscribers));
    }

    @Override
    public Subscription subscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        List<Subscription> subscriptions = new ArrayList<>();
        for (EventSubscriber subscriber : subscribers) {
            Subscription subscription = subscriber.subscribe(listener, endpoint, context);
            subscriptions.add(subscription);
        }
        return new ComposeSubscription(subscriptions).simplify();
    }
}
