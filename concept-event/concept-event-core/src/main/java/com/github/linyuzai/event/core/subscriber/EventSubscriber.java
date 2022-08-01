package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.util.function.Consumer;

/**
 * 事件订阅器
 */
public interface EventSubscriber {

    /**
     * 订阅事件
     *
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    void subscribe(Consumer<Object> consumer, EventEndpoint endpoint, EventContext context);
}
