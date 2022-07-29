package com.github.linyuzai.event.core.codec;

/**
 * 事件编码器
 */
public interface EventEncoder {

    /**
     * 编码
     *
     * @param event 事件
     * @return 编码后的事件
     */
    Object encode(Object event);
}
