package com.github.linyuzai.cloud.web.core.i18n.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当i18n启用
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(name = "concept.cloud.web.i18n.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnWebI18nEnabled {

}
