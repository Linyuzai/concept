package com.github.linyuzai.connection.loadbalance.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 连接负载均衡的基础配置注解。
 * <p>
 * Basic annotation for connection load balance.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ConnectionLoadBalanceConfiguration.class)
public @interface EnableConnectionLoadBalanceConfiguration {
}
