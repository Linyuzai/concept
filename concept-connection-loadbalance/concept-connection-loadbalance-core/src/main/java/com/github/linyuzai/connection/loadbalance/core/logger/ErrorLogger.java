package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;

/**
 * 异常日志
 */
public class ErrorLogger implements ErrorHandler {

    @Override
    public void onError(Throwable e, Object event, ConnectionLoadBalanceConcept concept) {
        concept.getLogger().error("Exception occurred", e);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
