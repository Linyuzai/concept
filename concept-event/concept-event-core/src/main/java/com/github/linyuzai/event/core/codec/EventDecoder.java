package com.github.linyuzai.event.core.codec;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

/**
 * 事件解码器
 */
public interface EventDecoder {

    /**
     * 解码
     *
     * @param event    事件
     * @param endpoint 事件端点
     * @param context  事件上下文
     * @return 解码后的事件
     */
    Object decode(Object event, EventEndpoint endpoint, EventContext context);
}
