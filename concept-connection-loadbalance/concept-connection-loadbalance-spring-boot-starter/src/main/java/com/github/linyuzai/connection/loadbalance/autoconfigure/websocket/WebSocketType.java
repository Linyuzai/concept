package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax.JavaxWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax.JavaxWebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax.JavaxWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive.ReactiveWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive.ReactiveWebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive.ReactiveWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet.ServletWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet.ServletWebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet.ServletWebSocketLoadBalanceConfiguration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum WebSocketType {

    /**
     * 根据当前环境自动进行配置
     */
    AUTO,

    /**
     * 使用 javax 配置
     */
    JAVAX(JavaxWebSocketDefaultEndpointConfiguration.class,
            JavaxWebSocketConceptConfiguration.class,
            JavaxWebSocketLoadBalanceConfiguration.class),

    /**
     * 使用 servlet 配置
     */
    SERVLET(ServletWebSocketDefaultEndpointConfiguration.class,
            ServletWebSocketConceptConfiguration.class,
            ServletWebSocketLoadBalanceConfiguration.class),

    /**
     * 使用 reactive 配置
     */
    REACTIVE(ReactiveWebSocketDefaultEndpointConfiguration.class,
            ReactiveWebSocketConceptConfiguration.class,
            ReactiveWebSocketLoadBalanceConfiguration.class);

    private final Class<?> defaultEndpoint;

    private final Class<?>[] configureClasses;

    WebSocketType() {
        this(null);
    }

    WebSocketType(Class<?> defaultEndpoint, Class<?>... configureClasses) {
        this.defaultEndpoint = defaultEndpoint;
        this.configureClasses = configureClasses;
    }

    public String[] getConfigurations(boolean enableDefaultEndpoint) {
        List<Class<?>> classes = new ArrayList<>(Arrays.asList(configureClasses));
        if (enableDefaultEndpoint && defaultEndpoint != null) {
            classes.add(defaultEndpoint);
        }
        return classes.stream()
                .map(Class::getName)
                .toArray(String[]::new);
    }
}
