package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.AbstractDomainEventPublisher;
import com.github.linyuzai.domain.core.event.DomainEventAdapter;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;

/**
 * 基于 {@link ApplicationEventPublisher} 的事件发布器
 */
public class ApplicationDomainEventPublisher extends AbstractDomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationDomainEventPublisher(ApplicationEventPublisher eventPublisher, List<DomainEventAdapter> eventAdapters) {
        super(eventAdapters);
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected void onPublish(Object event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    protected void onExchange(Collection<?> events) {
        events.forEach(eventPublisher::publishEvent);
    }
}
