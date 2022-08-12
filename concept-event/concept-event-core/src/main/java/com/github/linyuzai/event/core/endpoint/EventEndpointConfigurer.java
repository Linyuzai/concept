package com.github.linyuzai.event.core.endpoint;

/**
 * 事件端点扩展配置
 *
 * @param <E> 事件端点类型
 */
public interface EventEndpointConfigurer<E extends EventEndpoint> {

    /**
     * 配置事件端点
     */
    void configure(E endpoint);
}
