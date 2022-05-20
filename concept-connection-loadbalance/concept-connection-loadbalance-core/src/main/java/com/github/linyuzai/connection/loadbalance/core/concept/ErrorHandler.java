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
     * @param o 事件
     */
    void onError(Throwable e, Object o);

    @Override
    default void onEvent(Object event) {
        if (event instanceof ErrorEvent) {
            Throwable error = ((ErrorEvent) event).getError();
            onError(error, event);
        }
    }
}
