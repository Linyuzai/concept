package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ErrorLogger extends ConnectionLoadBalanceLogger implements ErrorHandler {

    public ErrorLogger(Consumer<String> info, BiConsumer<String, Throwable> error) {
        super(info, error);
    }

    @Override
    public void onError(Throwable e, Object o) {
        error("", e);
    }
}
