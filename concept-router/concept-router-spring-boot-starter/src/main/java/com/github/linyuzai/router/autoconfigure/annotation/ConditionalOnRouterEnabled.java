package com.github.linyuzai.router.autoconfigure.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnProperty(name = "concept.router.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnRouterEnabled {
}
