package com.github.linyuzai.event.core.codec;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;

/**
 * 事件编码器
 *
 * 在执行事件监听器 {@link com.github.linyuzai.event.core.listener.EventListener} 之前生效
 */
public interface EventEncoder {

    /**
     * 编码
     *
     * @param event    事件
     * @param endpoint 事件端点
     * @param context  事件上下文
     * @return 编码后的事件
     */
    Object encode(Object event, EventEndpoint endpoint, EventContext context);
}
