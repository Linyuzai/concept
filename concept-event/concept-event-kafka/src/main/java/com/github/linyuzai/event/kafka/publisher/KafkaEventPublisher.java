package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.AbstractEventPublisher;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;

public abstract class KafkaEventPublisher extends AbstractEventPublisher {


    @Override
    public void doPublish(Object event, EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof KafkaEventEndpoint) {
            publishKafka(event, (KafkaEventEndpoint) endpoint, context);
        }
    }

    public abstract void publishKafka(Object event, KafkaEventEndpoint endpoint, EventContext context);
}
