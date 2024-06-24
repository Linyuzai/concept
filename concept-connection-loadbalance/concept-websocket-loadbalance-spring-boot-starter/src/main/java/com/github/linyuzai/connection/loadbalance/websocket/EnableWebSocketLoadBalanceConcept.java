package com.github.linyuzai.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.EnableConnectionLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.reactive.ReactiveWebSocketLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.websocket.servlet.ServletWebSocketLoadBalanceConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ws 负载均衡的启用注解。
 * <p>
 * Enable annotation for ws load balancing.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConnectionLoadBalanceConfiguration
@EnableConfigurationProperties(WebSocketLoadBalanceProperties.class)
@Import({ServletWebSocketLoadBalanceConfiguration.class,
        ReactiveWebSocketLoadBalanceConfiguration.class})
public @interface EnableWebSocketLoadBalanceConcept {
}
