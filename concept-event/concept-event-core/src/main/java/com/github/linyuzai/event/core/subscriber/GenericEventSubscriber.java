package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

/**
 * 基于泛型的事件订阅器
 *
 * @param <T> 事件类型
 */
public interface GenericEventSubscriber<T> extends EventSubscriber {

    /**
     * 接收事件
     *
     * @param event    事件
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    void onEvent(T event, EventEndpoint endpoint, EventContext context);
}
