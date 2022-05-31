package com.github.linyuzai.connection.loadbalance.autoconfigure.scope;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

/**
 * 连接域
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(ConnectionScope.NAME)
public @interface ConnectionScope {

    String NAME = "Concept@connection";
}
