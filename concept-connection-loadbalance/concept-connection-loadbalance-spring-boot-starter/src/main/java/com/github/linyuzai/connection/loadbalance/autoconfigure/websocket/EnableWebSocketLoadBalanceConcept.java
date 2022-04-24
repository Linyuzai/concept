package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceConceptInitializer;
import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceDiscoveryConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ConnectionLoadBalanceDiscoveryConfiguration.class,
        WebSocketLoadBalanceConfiguration.class,
        WebSocketLoadBalanceSelector.class,
        ConnectionLoadBalanceConceptInitializer.class})
public @interface EnableWebSocketLoadBalanceConcept {

    ServerType type() default ServerType.AUTO;

    boolean defaultServer() default true;
}
