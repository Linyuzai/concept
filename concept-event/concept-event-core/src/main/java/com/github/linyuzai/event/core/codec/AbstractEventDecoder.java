package com.github.linyuzai.event.core.codec;

import java.lang.reflect.Type;

/**
 * 事件解码器抽象类
 * <p>
 * 前置处理：当事件或类型为 null 时直接返回
 */
public abstract class AbstractEventDecoder implements EventDecoder {
    @Override
    public Object decode(Object event, Type type) {
        if (type == null || event == null) {
            return event;
        }
        return doDecode(event, type);
    }

    public abstract Object doDecode(Object event, Type type);
}
