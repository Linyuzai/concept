package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventEngine implements EventEngine {

    @NonNull
    private String name;

    private Map<Object, Object> metadata;

    private EventPublisher publisher;

    private EventSubscriber subscriber;

    private final Map<String, EventEndpoint> endpointMap = new ConcurrentHashMap<>();

    @Override
    public void add(Collection<? extends EventEndpoint> endpoints) {
        for (EventEndpoint endpoint : endpoints) {
            this.endpointMap.put(endpoint.getName(), endpoint);
        }
    }

    public Collection<EventEndpoint> getEndpoints() {
        return Collections.unmodifiableCollection(endpointMap.values());
    }
}
