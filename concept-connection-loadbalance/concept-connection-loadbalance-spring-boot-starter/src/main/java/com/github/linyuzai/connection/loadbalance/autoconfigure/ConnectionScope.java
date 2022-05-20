package com.github.linyuzai.connection.loadbalance.autoconfigure;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(ConnectionScope.NAME)
public @interface ConnectionScope {

    String NAME = "connection";
}
