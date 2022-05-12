package com.github.linyuzai.connection.loadbalance.core.event;

public interface EventPublishErrorHandler {

    void onEventPublishError(Object event, Throwable e);
}
