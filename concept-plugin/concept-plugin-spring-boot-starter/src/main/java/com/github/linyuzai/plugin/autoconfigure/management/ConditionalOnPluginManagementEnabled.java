package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.autoconfigure.autoload.ConditionalOnPluginAutoloadEnabled;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Plugin Management 被启用的条件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnPluginAutoloadEnabled
@ConditionalOnProperty(name = "concept.plugin.management.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnPluginManagementEnabled {
}
