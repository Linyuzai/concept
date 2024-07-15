package com.github.linyuzai.plugin.autoconfigure.autoload;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Plugin Auto Load 被启用的条件注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(name = "concept.plugin.autoload.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnPluginAutoloadEnabled {
}
