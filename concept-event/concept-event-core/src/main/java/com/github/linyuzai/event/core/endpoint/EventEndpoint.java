package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.concept.EventTemplate;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.engine.EventEngine;

import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * 事件端点
 * <p>
 * 发布订阅事件的操作单元
 */
public interface EventEndpoint extends EventTemplate.InstanceConfig {

    /**
     * 端点名称
     */
    String getName();

    /**
     * 获得所属的事件引擎
     */
    EventEngine getEngine();

    /**
     * 发布事件
     */
    void publish(Object event, EventContext context);

    /**
     * 订阅事件
     */
    void subscribe(Consumer<Object> consumer, EventContext context);
}
