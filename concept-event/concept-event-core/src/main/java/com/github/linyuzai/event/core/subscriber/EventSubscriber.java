package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.EventListener;


/**
 * 事件订阅者
 */
public interface EventSubscriber {

    /**
     * 订阅事件
     *
     * @param listener 事件监听器
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    void subscribe(EventListener listener, EventEndpoint endpoint, EventContext context);
}
