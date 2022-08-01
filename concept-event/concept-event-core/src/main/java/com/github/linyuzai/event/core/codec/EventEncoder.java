package com.github.linyuzai.event.core.codec;

import com.github.linyuzai.event.core.context.EventContext;

/**
 * 事件编码器
 */
public interface EventEncoder {

    /**
     * 编码
     *
     * @param event   事件
     * @param context 事件上下文
     * @return 编码后的事件
     */
    Object encode(Object event, EventContext context);
}
