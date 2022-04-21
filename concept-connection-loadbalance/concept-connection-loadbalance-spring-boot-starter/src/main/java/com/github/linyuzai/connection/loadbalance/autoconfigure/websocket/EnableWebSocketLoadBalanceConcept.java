package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ReactiveWebSocketConfiguration.class,
        WebSocketDiscoveryConfiguration.class,
        WebSocketLoadBalanceConfiguration.class})
public @interface EnableWebSocketLoadBalanceConcept {

}
