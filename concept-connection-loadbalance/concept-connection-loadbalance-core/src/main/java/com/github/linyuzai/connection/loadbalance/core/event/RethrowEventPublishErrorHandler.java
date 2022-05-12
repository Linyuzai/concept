package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.SneakyThrows;

public class RethrowEventPublishErrorHandler implements EventPublishErrorHandler {

    @SneakyThrows
    @Override
    public void onEventPublishError(Object event, Throwable e) {
        throw e;
    }
}
