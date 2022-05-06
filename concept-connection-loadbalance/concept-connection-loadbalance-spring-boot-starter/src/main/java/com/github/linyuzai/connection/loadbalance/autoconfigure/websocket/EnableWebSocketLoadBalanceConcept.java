package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceConceptInitializer;
import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(WebSocketLoadBalanceProperties.class)
@Import({ConnectionLoadBalanceConfiguration.class,
        WebSocketLoadBalanceConfiguration.class,
        WebSocketLoadBalanceImportSelector.class,
        ConnectionLoadBalanceConceptInitializer.class})
public @interface EnableWebSocketLoadBalanceConcept {
}
