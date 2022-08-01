package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.template.EventTemplate;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.util.Arrays;
import java.util.Collection;

/**
 * 事件引擎
 * <p>
 * 包含多个事件端点
 */
public interface EventEngine extends EventTemplate.InstanceConfig {

    /**
     * 获得引擎名称
     */
    String getName();

    /**
     * 根据名称获得事件端点
     */
    EventEndpoint getEndpoint(String name);

    /**
     * 获得所有的事件端点
     */
    Collection<EventEndpoint> getEndpoints();

    /**
     * 添加事件端点
     */
    default void addEndpoints(EventEndpoint... endpoints) {
        addEndpoints(Arrays.asList(endpoints));
    }

    /**
     * 添加事件端点
     */
    void addEndpoints(Collection<? extends EventEndpoint> endpoints);

    /**
     * 根据名称移除事件端点
     */
    default void removeEndpoints(String... endpoints) {
        removeEndpoints(Arrays.asList(endpoints));
    }

    /**
     * 根据名称移除事件端点
     */
    void removeEndpoints(Collection<String> endpoints);
}
