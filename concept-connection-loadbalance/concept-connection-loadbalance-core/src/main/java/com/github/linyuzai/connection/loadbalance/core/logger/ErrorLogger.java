package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
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
    public void onError(Throwable e, Object event, ConnectionLoadBalanceConcept concept) {
        error("Exception occurred", e);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
