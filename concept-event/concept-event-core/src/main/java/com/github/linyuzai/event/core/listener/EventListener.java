package com.github.linyuzai.event.core.listener;

import java.lang.reflect.Type;

/**
 * 事件监听器
 */
public interface EventListener {

    /**
     * 监听事件
     */
    void onEvent(Object event);

    /**
     * 事件类型
     */
    Type getType();
}
