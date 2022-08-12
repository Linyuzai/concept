package com.github.linyuzai.event.core.config;

/**
 * 事件端点配置
 */
public interface EndpointConfig extends PropertiesConfig {

    /**
     * 是否启用
     */
    boolean isEnabled();

    /**
     * 获得继承的端点
     */
    String getInherit();
}
