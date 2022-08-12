package com.github.linyuzai.event.core.config;

import java.util.Map;

/**
 * 事件引擎配置
 */
public interface EngineConfig extends PropertiesConfig {

    /**
     * 获得所有的端点配置
     */
    Map<String, ? extends EndpointConfig> getEndpoints();
}
