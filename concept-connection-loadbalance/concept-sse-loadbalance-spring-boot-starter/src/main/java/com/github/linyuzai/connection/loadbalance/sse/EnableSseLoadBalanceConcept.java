package com.github.linyuzai.connection.loadbalance.sse;

import com.github.linyuzai.connection.loadbalance.autoconfigure.EnableConnectionLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.reactive.ReactiveSseLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.servlet.ServletSseLoadBalanceConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * SSE 负载均衡的启用注解。
 * <p>
 * Enable annotation for SSE load balancing.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConnectionLoadBalanceConfiguration
@EnableConfigurationProperties(SseLoadBalanceProperties.class)
@Import({ServletSseLoadBalanceConfiguration.class,
        ReactiveSseLoadBalanceConfiguration.class})
public @interface EnableSseLoadBalanceConcept {
}
