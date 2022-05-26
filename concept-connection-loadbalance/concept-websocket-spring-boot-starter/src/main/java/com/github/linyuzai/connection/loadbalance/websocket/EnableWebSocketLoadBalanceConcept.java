package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceConceptInitializer;
import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ws 负载均衡的启用注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(WebSocketLoadBalanceProperties.class)
@Import({WebSocketScopeName.class,
        ConnectionLoadBalanceConfiguration.class,
        WebSocketLoadBalanceConfiguration.class,
        WebSocketLoadBalanceImportSelector.class,
        ConnectionLoadBalanceConceptInitializer.class})
public @interface EnableWebSocketLoadBalanceConcept {
}
