package com.github.linyuzai.connection.loadbalance.netty;

import com.github.linyuzai.connection.loadbalance.autoconfigure.EnableConnectionLoadBalanceConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Netty 负载均衡的启用注解。
 * <p>
 * Enable annotation for Netty load balancing.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConnectionLoadBalanceConfiguration
@EnableConfigurationProperties(NettyLoadBalanceProperties.class)
@Import(NettyLoadBalanceConfiguration.class)
public @interface EnableNettyLoadBalanceConcept {
}
