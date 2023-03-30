package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractDomainEventPublisher implements DomainEventPublisher {

    private DomainContext context;

    @Override
    public void publish(Object event) {
        onPublish(event);
        doPublish(event);
    }

    protected void onPublish(Object event) {
        if (event instanceof DomainEvent) {
            ((DomainEvent) event).onPublish(context, this);
        }
    }

    protected abstract void doPublish(Object event);
}
