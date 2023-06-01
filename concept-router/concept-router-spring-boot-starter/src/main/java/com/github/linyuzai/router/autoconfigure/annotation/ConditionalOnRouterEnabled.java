package com.github.linyuzai.router.autoconfigure.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.lang.annotation.*;

/**
 * Router 被启用的条件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnClass(name = "org.springframework.cloud.client.serviceregistry.Registration")
@ConditionalOnProperty(name = "concept.router.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnRouterEnabled {
}
