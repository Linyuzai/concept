package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceDiscoveryConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ConnectionLoadBalanceDiscoveryConfiguration.class,
        ReactiveWebSocketConfiguration.class,
        WebSocketLoadBalanceConfiguration.class})
public @interface EnableWebSocketLoadBalanceConcept {

}
