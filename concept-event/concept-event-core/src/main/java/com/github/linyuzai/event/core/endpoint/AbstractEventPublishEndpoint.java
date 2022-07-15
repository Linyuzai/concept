package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventPublishEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractEventPublishEndpoint implements EventPublishEndpoint {

    private String name;

    private Map<Object, Object> metadata;

    private EventPublishEngine engine;

    private EventPublisher defaultPublisher;

    @Override
    public void publish(Object event, EventPublisher publisher) {
        EventPublisher handlerToUse = publisher == null ? getDefaultPublisher() : publisher;
        handlerToUse.publish(event, this);
    }
}
