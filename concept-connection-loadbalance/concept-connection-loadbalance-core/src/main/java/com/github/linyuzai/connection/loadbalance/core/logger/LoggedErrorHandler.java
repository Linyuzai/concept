package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ErrorHandler;

/**
 * 异常日志打印。
 * <p>
 * Handler to log error.
 */
public class LoggedErrorHandler implements ErrorHandler {

    @Override
    public void onError(Throwable e, Object event, ConnectionLoadBalanceConcept concept) {
        concept.getLogger().error("Exception occurred", e);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
