package com.github.linyuzai.event.core.bus;

import com.github.linyuzai.event.core.config.InstanceConfig;

/**
 * 事件总线抽象
 */
public interface EventBus extends InstanceConfig {

    /**
     * 初始化
     */
    void initialize();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 发布事件
     */
    void publish(Object event);
}
