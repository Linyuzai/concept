package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketDefaultEndpointConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketDefaultEndpointConfiguration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    JAVAX(JavaxWebSocketDefaultEndpointConfiguration.class,
            JavaxWebSocketConceptConfiguration.class),

    /**
     * 使用 servlet 配置
     */
    SERVLET(ServletWebSocketDefaultEndpointConfiguration.class,
            ServletWebSocketConceptConfiguration.class),

    /**
     * 使用 reactive 配置
     */
    REACTIVE(ReactiveWebSocketDefaultEndpointConfiguration.class,
            ReactiveWebSocketConceptConfiguration.class);

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
