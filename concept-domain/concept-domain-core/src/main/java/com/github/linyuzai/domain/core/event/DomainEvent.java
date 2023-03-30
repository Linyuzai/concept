package com.github.linyuzai.domain.core.event;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainEventPublisher;

public interface DomainEvent {

    void onPublish(DomainContext context, DomainEventPublisher publisher);
}
