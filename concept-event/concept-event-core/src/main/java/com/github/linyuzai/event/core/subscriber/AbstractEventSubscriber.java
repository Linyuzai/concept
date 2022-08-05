package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.core.listener.EventListener;

import java.util.function.Consumer;

/**
 * 事件订阅器的抽象类
 * <p>
 * 处理事件解码
 */
public abstract class AbstractEventSubscriber implements EventSubscriber {

    @Override
    public Subscription subscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        if (support(endpoint, context)) {
            Subscription subscription = doSubscribe(endpoint, context, o -> {
                EventDecoder decoder = context.get(EventDecoder.class);
                //解码事件
                Object decoded = decoder == null ? o : decoder.decode(o, endpoint, context);
                listener.onEvent(decoded, endpoint, context);
            });
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

    public abstract Subscription doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer);

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
