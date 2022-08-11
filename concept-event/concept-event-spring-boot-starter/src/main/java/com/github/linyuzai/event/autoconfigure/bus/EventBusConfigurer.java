package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.EventBus;

/**
 * 事件总线自定义配置扩展类
 */
public interface EventBusConfigurer {

    /**
     * 可自定义配置事件总线
     */
    void configure(EventBus bus);
}
