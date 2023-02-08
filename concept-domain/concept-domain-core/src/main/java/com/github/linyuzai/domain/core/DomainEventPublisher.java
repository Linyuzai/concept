package com.github.linyuzai.domain.core;

/**
 * 领域事件发布器
 */
public interface DomainEventPublisher {

    void publish(Object event);
}
