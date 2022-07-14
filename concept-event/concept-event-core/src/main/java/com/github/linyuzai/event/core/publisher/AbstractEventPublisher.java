package com.github.linyuzai.event.core.publisher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEventPublisher implements EventPublisher {

    private EventPublisherGroup group;
}
