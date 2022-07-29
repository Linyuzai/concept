package com.github.linyuzai.event.core.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

/**
 * 事件发布器
 */
public interface EventPublisher {

    /**
     * 发布事件
     *
     * @param event    事件
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    void publish(Object event, EventEndpoint endpoint, EventContext context);
}
