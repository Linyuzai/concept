package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件端点的抽象类
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventEngine implements EventEngine {

    /**
     *
     */
    @NonNull
    private final String name;

    private Map<Object, Object> metadata;

    private EventEncoder encoder;

    private EventDecoder decoder;

    private EventErrorHandler errorHandler;

    private EventPublisher publisher;

    private EventSubscriber subscriber;

    private final Map<String, EventEndpoint> endpointMap = new ConcurrentHashMap<>();

    @Override
    public EventEndpoint getEndpoint(String name) {
        return endpointMap.get(name);
    }

    public Collection<EventEndpoint> getEndpoints() {
        return Collections.unmodifiableCollection(endpointMap.values());
    }

    @Override
    public void addEndpoints(Collection<? extends EventEndpoint> endpoints) {
        for (EventEndpoint endpoint : endpoints) {
            this.endpointMap.put(endpoint.getName(), endpoint);
        }
    }

    @Override
    public void removeEndpoints(Collection<String> endpoints) {
        for (String endpoint : endpoints) {
            this.endpointMap.remove(endpoint);
        }
    }
}
