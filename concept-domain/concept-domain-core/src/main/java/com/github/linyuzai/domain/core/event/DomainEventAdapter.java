package com.github.linyuzai.domain.core.event;

public interface DomainEventAdapter {

    boolean support(Object event);

    Object adapt(Object event);
}
