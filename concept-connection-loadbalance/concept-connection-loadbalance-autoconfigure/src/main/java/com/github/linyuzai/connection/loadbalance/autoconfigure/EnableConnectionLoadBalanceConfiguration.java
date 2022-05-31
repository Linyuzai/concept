package com.github.linyuzai.connection.loadbalance.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 连接负载均衡的基础配置注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(ConnectionLoadBalanceProperties.class)
@Import({ConnectionLoadBalanceConfiguration.class,
        ConnectionLoadBalanceConceptInitializer.class})
public @interface EnableConnectionLoadBalanceConfiguration {
}
