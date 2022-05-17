package com.github.linyuzai.connection.loadbalance.core.utils;

import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@AllArgsConstructor
public class ConnectionLoadBalanceLogger {

    private Consumer<String> info;

    private BiConsumer<String, Throwable> error;

    public String appendTag(String msg) {
        return "LBWebSocket >> " + msg;
    }

    public void info(String msg) {
        info.accept(appendTag(msg));
    }

    public void error(String msg, Throwable e) {
        error.accept(appendTag(msg), e);
    }
}
