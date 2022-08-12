package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.config.EndpointConfig;
import com.github.linyuzai.event.core.engine.EventEngine;

/**
 * 事件端点工厂
 *
 * @param <C>        事件端点配置类型
 * @param <Engine>   事件引擎类型
 * @param <Endpoint> 事件端点类型
 */
public interface EventEndpointFactory<C extends EndpointConfig, Engine extends EventEngine, Endpoint extends EventEndpoint> {

    /**
     * 创建事件端点
     */
    Endpoint create(String name, C config, Engine engine);
}
