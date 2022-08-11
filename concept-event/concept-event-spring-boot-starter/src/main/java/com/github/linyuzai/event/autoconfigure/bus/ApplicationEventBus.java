package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.AbstractEventBus;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.exchange.EventExchange;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 基于 {@link ApplicationEventPublisher} 的事件总线
 */
@Getter
public class ApplicationEventBus extends AbstractEventBus {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationEventBus(EventConcept concept, EventExchange exchange, ApplicationEventPublisher eventPublisher) {
        super(concept, exchange);
        this.eventPublisher = eventPublisher;
    }

    /**
     * 在发布事件时通过 {@link ApplicationEventPublisher} 发布本地事件
     */
    @Override
    public void onPublish(Object event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 监听到事件时通过 {@link ApplicationEventPublisher} 发布本地事件
     */
    @Override
    public void onEvent(Object event) {
        eventPublisher.publishEvent(event);
    }
}
