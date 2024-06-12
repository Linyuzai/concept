package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * Plugin Management 被启用的条件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(name = "concept.plugin.management.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnPluginManagementEnabled {
}
