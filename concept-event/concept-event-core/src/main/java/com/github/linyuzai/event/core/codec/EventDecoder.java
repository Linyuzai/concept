package com.github.linyuzai.event.core.codec;

import java.lang.reflect.Type;

/**
 * 事件解码器
 */
public interface EventDecoder {

    /**
     * 解码
     *
     * @param event 事件
     * @param type  解码类型
     * @return 解码后的事件
     */
    Object decode(Object event, Type type);
}
