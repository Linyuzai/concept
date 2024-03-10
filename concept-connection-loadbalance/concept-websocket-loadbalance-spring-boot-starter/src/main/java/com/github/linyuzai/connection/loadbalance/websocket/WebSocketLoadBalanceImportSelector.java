package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceEnvironment;
import lombok.NonNull;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

/**
 * ws 负载均衡的导入选择器。
 * <p>
 * Import selector for ws load balancing.
 */
public class WebSocketLoadBalanceImportSelector extends ConnectionLoadBalanceEnvironment implements ImportSelector {

    @Override
    public String @NonNull [] selectImports(@NonNull AnnotationMetadata metadata) {
        WebSocketType type = getType();
        return Arrays.stream(type.getConfigurationClasses())
                .map(Class::getName)
                .toArray(String[]::new);
    }

    private WebSocketType getType() {
        WebSocketType type = getEnvironment().getProperty("concept.websocket.type",
                WebSocketType.class, WebSocketType.AUTO);
        if (type == WebSocketType.AUTO) {
            return deduceServerType();
        }
        return type;
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
}
