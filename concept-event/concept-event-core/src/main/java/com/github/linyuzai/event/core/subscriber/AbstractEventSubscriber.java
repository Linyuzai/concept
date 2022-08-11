package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.core.listener.EventListener;

/**
 * 事件订阅器的抽象类
 * <p>
 * 处理事件解码
 */
public abstract class AbstractEventSubscriber implements EventSubscriber {

    @Override
    public Subscription subscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        if (support(endpoint, context)) {
            Subscription subscription = doSubscribe(listener, endpoint, context);
            addLifecycleListener(context, subscription);
            return subscription;
        } else {
            return Subscription.EMPTY;
        }
    }

    public void addLifecycleListener(EventContext context, Subscription subscription) {
        EventConcept concept = context.get(EventConcept.class);
        concept.addLifecycleListeners(new UnSubscriber(concept, subscription));
    }

    public abstract boolean support(EventEndpoint endpoint, EventContext context);

    public abstract Subscription doSubscribe(EventListener listener, EventEndpoint endpoint, EventContext context);

    public static class UnSubscriber implements EventConceptLifecycleListener {

        private final Subscription subscription;

        public UnSubscriber(EventConcept concept, Subscription subscription) {
            this.subscription = new PostSubscription(subscription, () ->
                    concept.removeLifecycleListeners(UnSubscriber.this));
        }

        @Override
        public void onInitialize(EventConcept concept) {

        }

        @Override
        public void onDestroy(EventConcept concept) {
            subscription.unsubscribe();
        }
    }
}
