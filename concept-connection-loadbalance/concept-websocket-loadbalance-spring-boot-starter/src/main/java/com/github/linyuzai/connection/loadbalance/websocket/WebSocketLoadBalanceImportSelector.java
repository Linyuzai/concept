package com.github.linyuzai.connection.loadbalance.websocket;

import lombok.NonNull;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebEnvironment;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.WebApplicationContext;

public class WebSocketLoadBalanceImportSelector implements ImportSelector, EnvironmentAware {

    private Environment environment;

    @Override
    public String @NonNull [] selectImports(@NonNull AnnotationMetadata metadata) {
        WebSocketType type = environment.getProperty("concept.websocket.type",
                WebSocketType.class, WebSocketType.AUTO);
        if (type == WebSocketType.AUTO) {
            type = deduceServerType();
        }
        boolean enableDefaultEndpoint = environment.getProperty("concept.websocket.server.default-endpoint.enabled",
                boolean.class, true);
        return type.getConfigurations(enableDefaultEndpoint);
    }

    /**
     * 推断服务类型
     *
     * @return 应用的类型
     */
    private WebSocketType deduceServerType() {
        if (isServletWebApplication()) {
            return WebSocketType.SERVLET;
        } else if (isReactiveWebApplication()) {
            return WebSocketType.REACTIVE;
        }
        throw new IllegalArgumentException("WebSocket type can not deduce");
    }

    /**
     * 是否是 Servlet 环境
     *
     * @return 是否是 Servlet 环境
     */
    private boolean isServletWebApplication() {
        if (environment instanceof ConfigurableWebEnvironment) {
            return true;
        }
        if (environment instanceof WebApplicationContext) {
            return true;
        }
        return false;
    }

    /**
     * 是否是 Reactive 环境
     *
     * @return 是否是 Reactive 环境
     */
    private boolean isReactiveWebApplication() {
        if (environment instanceof ConfigurableReactiveWebEnvironment) {
            return true;
        }
        if (environment instanceof ReactiveWebApplicationContext) {
            return true;
        }
        return false;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
