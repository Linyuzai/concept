package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;

public abstract class KafkaEventSubscriber extends AbstractEventSubscriber {

    @Override
    public Subscription doSubscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        return subscribeKafka(listener, (KafkaEventEndpoint) endpoint, context);
    }

    @Override
    public boolean support(EventEndpoint endpoint, EventContext context) {
        return endpoint instanceof KafkaEventEndpoint;
    }

    public abstract Subscription subscribeKafka(EventListener listener,
                                                KafkaEventEndpoint endpoint,
                                                EventContext context);
}
