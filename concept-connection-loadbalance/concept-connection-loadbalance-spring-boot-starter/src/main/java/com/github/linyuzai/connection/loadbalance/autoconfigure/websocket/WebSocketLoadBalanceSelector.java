package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import lombok.NonNull;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebEnvironment;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

public class WebSocketLoadBalanceSelector implements ImportSelector, EnvironmentAware {

    private Environment environment;

    @Override
    public String @NonNull [] selectImports(@NonNull AnnotationMetadata metadata) {
        Map<String, Object> attributes = metadata
                .getAnnotationAttributes(EnableWebSocketLoadBalanceConcept.class.getName());
        if (attributes == null) {
            throw new IllegalArgumentException("@EnableWebSocketLoadBalanceConcept not found");
        }
        ServerType type = (ServerType) attributes.get("type");
        boolean defaultServer = (boolean) attributes.get("defaultServer");

        if (type == ServerType.AUTO) {
            type = deduceServerType();
        }

        return type.getConfigureClassNames(defaultServer);
    }

    private ServerType deduceServerType() {
        if (isServletWebApplication()) {
            return ServerType.SERVLET;
        } else if (isReactiveWebApplication()) {
            return ServerType.REACTIVE;
        }
        throw new IllegalArgumentException("Server type can not deduce");
    }

    private boolean isServletWebApplication() {
        if (environment instanceof ConfigurableWebEnvironment) {
            return true;
        }
        if (environment instanceof WebApplicationContext) {
            return true;
        }
        return false;
    }

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
