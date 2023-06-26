package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;

/**
 * 异常处理器
 */
public interface ErrorHandler extends ConnectionEventListener {

    /**
     * 异常回调
     *
     * @param e 异常
     * @param event 事件
     */
    void onError(Throwable e, Object event, ConnectionLoadBalanceConcept concept);

    @Override
    default void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ErrorEvent) {
            Throwable error = ((ErrorEvent) event).getError();
            onError(error, event, concept);
        }
    }
}
