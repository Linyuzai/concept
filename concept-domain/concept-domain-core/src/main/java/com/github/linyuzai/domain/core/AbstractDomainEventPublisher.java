package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.event.DomainEventAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public abstract class AbstractDomainEventPublisher implements DomainEventPublisher {

    private List<DomainEventAdapter> eventAdapters;

    @Override
    public void publish(Object event) {
        onPublish(event);
        if (eventAdapters != null) {
            List<Object> events = eventAdapters.stream()
                    .filter(it -> it.support(event))
                    .map(it -> it.adapt(event))
                    .collect(Collectors.toList());
            onExchange(events);
        }
    }

    protected abstract void onPublish(Object event);

    protected abstract void onExchange(Collection<?> events);
}
