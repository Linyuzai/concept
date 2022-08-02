package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;

import java.util.function.Consumer;

public abstract class KafkaEventSubscriber extends AbstractEventSubscriber {

    @Override
    public Subscription doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        return subscribeKafka((KafkaEventEndpoint) endpoint, context, consumer);
    }

    @Override
    public boolean support(EventEndpoint endpoint, EventContext context) {
        return endpoint instanceof KafkaEventEndpoint;
    }

    public abstract Subscription subscribeKafka(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
