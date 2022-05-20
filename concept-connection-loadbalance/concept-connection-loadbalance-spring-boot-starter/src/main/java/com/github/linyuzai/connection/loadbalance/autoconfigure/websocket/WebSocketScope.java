package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebSocketScope.NAME)
public @interface WebSocketScope {

    String NAME = "websocket";
}
