package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.EnableConnectionLoadBalanceConfiguration;
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
@EnableConnectionLoadBalanceConfiguration
@EnableConfigurationProperties(WebSocketLoadBalanceProperties.class)
@Import({WebSocketLoadBalanceImportSelector.class, WebSocketLoadBalanceConfiguration.class})
public @interface EnableWebSocketLoadBalanceConcept {
}
