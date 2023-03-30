package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.AbstractDomainEventPublisher;
import com.github.linyuzai.domain.core.DomainContext;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 基于 {@link ApplicationEventPublisher} 的事件发布器
 */
public class ApplicationDomainEventPublisher extends AbstractDomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationDomainEventPublisher(DomainContext context, ApplicationEventPublisher eventPublisher) {
        super(context);
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected void doPublish(Object event) {
        eventPublisher.publishEvent(event);
    }
}
