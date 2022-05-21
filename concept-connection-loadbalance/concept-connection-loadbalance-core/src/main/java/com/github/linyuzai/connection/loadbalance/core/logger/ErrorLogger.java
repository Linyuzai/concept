package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 异常日志
 */
public class ErrorLogger extends ConnectionLoadBalanceLogger implements ErrorHandler {

    public ErrorLogger(Consumer<String> info, BiConsumer<String, Throwable> error) {
        super(info, error);
    }

    @Override
    public void onError(Throwable e, Object o) {
        error("Exception occurred", e);
    }
}
