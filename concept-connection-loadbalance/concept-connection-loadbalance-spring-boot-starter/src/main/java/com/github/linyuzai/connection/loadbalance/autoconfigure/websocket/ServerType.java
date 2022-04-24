package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax.JavaxWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax.JavaxWebSocketDefaultServerConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.javax.JavaxWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive.ReactiveWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive.ReactiveWebSocketDefaultServerConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.reactive.ReactiveWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet.ServletWebSocketConceptConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet.ServletWebSocketDefaultServerConfiguration;
import com.github.linyuzai.connection.loadbalance.autoconfigure.websocket.servlet.ServletWebSocketLoadBalanceConfiguration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ServerType {

    /**
     * 根据当前环境自动进行默认配置
     */
    AUTO(null),

    /**
     * 使用 javax 配置默认服务
     */
    JAVAX(JavaxWebSocketDefaultServerConfiguration.class,
            JavaxWebSocketConceptConfiguration.class,
            JavaxWebSocketLoadBalanceConfiguration.class),

    /**
     * 使用 servlet 配置默认服务
     */
    SERVLET(ServletWebSocketDefaultServerConfiguration.class,
            ServletWebSocketConceptConfiguration.class,
            ServletWebSocketLoadBalanceConfiguration.class),

    /**
     * 使用 reactive 配置默认服务
     */
    REACTIVE(ReactiveWebSocketDefaultServerConfiguration.class,
            ReactiveWebSocketConceptConfiguration.class,
            ReactiveWebSocketLoadBalanceConfiguration.class);

    private final Class<?> defaultServer;

    private final Class<?>[] configureClasses;

    ServerType(Class<?> defaultServer, Class<?>... configureClasses) {
        this.defaultServer = defaultServer;
        this.configureClasses = configureClasses;
    }

    public String[] getConfigureClassNames(boolean configureDefaultServer) {
        List<Class<?>> classes = new ArrayList<>(Arrays.asList(configureClasses));
        if (configureDefaultServer) {
            classes.add(defaultServer);
        }
        return classes.stream()
                .map(Class::getName)
                .toArray(String[]::new);
    }
}
