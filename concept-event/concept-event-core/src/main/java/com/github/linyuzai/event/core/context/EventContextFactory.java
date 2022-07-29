package com.github.linyuzai.event.core.context;

/**
 * 事件上下文工厂
 */
public interface EventContextFactory {

    /**
     * 创建事件上下文
     */
    EventContext create();
}
