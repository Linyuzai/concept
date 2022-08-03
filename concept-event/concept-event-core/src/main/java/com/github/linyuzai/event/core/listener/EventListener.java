package com.github.linyuzai.event.core.listener;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

import java.lang.reflect.Type;

/**
 * 事件监听器
 */
public interface EventListener {

    /**
     * 监听事件
     */
    void onEvent(Object event, EventEndpoint endpoint, EventContext context);

    /**
     * 事件类型
     */
    Type getType();
}
