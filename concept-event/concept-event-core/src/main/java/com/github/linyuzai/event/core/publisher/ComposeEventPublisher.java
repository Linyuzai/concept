package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class ComposeEventPublisher implements EventPublisher {

    private final Collection<EventPublisher> publishers;

    public ComposeEventPublisher(EventPublisher... publishers) {
        this(Arrays.asList(publishers));
    }

    @Override
    public void publish(Object event, EventEndpoint endpoint, EventContext context) {
        for (EventPublisher publisher : publishers) {
            publisher.publish(event, endpoint, context);
        }
    }
}
