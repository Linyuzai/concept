package com.github.linyuzai.connection.loadbalance.websocket;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

/**
 * ws 连接域
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebSocketScope.NAME)
public @interface WebSocketScope {

    String NAME = "websocket";
}
