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

/**
 * 组合的事件订阅器
 */
@Getter
@AllArgsConstructor
public class ComposeEventSubscriber implements EventSubscriber {

    /**
     * 多个事件订阅器
     */
    private final Collection<EventSubscriber> subscribers;

    public ComposeEventSubscriber(EventSubscriber... subscribers) {
        this(Arrays.asList(subscribers));
    }

    /**
     * 遍历所有事件订阅器进行订阅
     */
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
