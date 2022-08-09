package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.config.AbstractInstanceConfig;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
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
public abstract class AbstractEventEngine extends AbstractInstanceConfig implements EventEngine {

    /**
     * 引擎名称
     */
    @NonNull
    private final String name;

    /**
     * 事件端点缓存
     */
    private final Map<String, EventEndpoint> endpointMap = new ConcurrentHashMap<>();

    @Override
    public EventEndpoint getEndpoint(String name) {
        return endpointMap.get(name);
    }

    public Collection<? extends EventEndpoint> getEndpoints() {
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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventEngine &&
                name.equals(((EventEngine) obj).getName());
    }
}
