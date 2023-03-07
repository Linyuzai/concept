package com.github.linyuzai.cloud.web.core.intercept.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnWebInterceptionEnabled
@ConditionalOnProperty(name = "concept.cloud.web.intercept.request.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnWebRequestInterceptionEnabled {

}
