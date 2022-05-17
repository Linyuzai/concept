package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;

public interface ErrorHandler extends ConnectionEventListener {

    void onError(Throwable e, Object o);

    @Override
    default void onEvent(Object event) {
        if (event instanceof ErrorEvent) {
            Throwable error = ((ErrorEvent) event).getError();
            onError(error, event);
        }
    }
}
