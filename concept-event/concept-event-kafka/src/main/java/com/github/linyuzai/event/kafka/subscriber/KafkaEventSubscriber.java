package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;

import java.util.function.Consumer;

public abstract class KafkaEventSubscriber extends AbstractEventSubscriber {

    @Override
    public void doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        if (endpoint instanceof KafkaEventEndpoint) {
            subscribeKafka((KafkaEventEndpoint) endpoint, context, consumer);
        }
    }

    public abstract void subscribeKafka(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
