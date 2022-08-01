package com.github.linyuzai.event.core.codec;

import com.github.linyuzai.event.core.context.EventContext;

/**
 * 事件解码器
 */
public interface EventDecoder {

    /**
     * 解码
     *
     * @param event 事件
     * @return 解码后的事件
     */
    Object decode(Object event, EventContext context);
}
