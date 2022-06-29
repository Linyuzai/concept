package com.github.linyuzai.router.autoconfigure.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * Router Management 被启用的条件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnRouterEnabled
@ConditionalOnProperty(name = "concept.router.management.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnRouterManagementEnabled {
}
