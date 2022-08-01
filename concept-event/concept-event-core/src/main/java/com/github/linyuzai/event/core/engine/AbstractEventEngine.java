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
     * 引擎名称
     */
    @NonNull
    private final String name;

    /**
     * 元数据
     */
    private Map<Object, Object> metadata;

    /**
     * 事件编码器
     */
    private EventEncoder encoder;

    /**
     * 事件解码器
     */
    private EventDecoder decoder;

    /**
     * 异常处理器
     */
    private EventErrorHandler errorHandler;

    /**
     * 事件发布器
     */
    private EventPublisher publisher;

    /**
     * 事件订阅器
     */
    private EventSubscriber subscriber;

    /**
     * 事件端点缓存
     */
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
