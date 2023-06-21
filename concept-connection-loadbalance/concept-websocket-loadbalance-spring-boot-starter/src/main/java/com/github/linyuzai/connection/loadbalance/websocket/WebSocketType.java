package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketLoadBalanceConfiguration;
import lombok.Getter;

/**
 * ws 类型
 */
@Getter
public enum WebSocketType {

    /**
     * 根据当前环境自动进行配置
     */
    AUTO,

    /**
     * 使用 javax 配置
     */
    JAVAX(JavaxWebSocketLoadBalanceConfiguration.class),

    /**
     * 使用 servlet 配置
     */
    SERVLET(ServletWebSocketLoadBalanceConfiguration.class),

    /**
     * 使用 reactive 配置
     */
    REACTIVE(ReactiveWebSocketLoadBalanceConfiguration.class);

    private final Class<?>[] configurationClasses;

    WebSocketType(Class<?>... configurationClasses) {
        this.configurationClasses = configurationClasses;
    }
}
