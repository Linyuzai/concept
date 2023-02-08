package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.DomainEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 基于 {@link ApplicationEventPublisher} 的事件发布器
 */
@AllArgsConstructor
public class ApplicationDomainEventPublisher implements DomainEventPublisher {

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(Object event) {
        eventPublisher.publishEvent(event);
    }
}
