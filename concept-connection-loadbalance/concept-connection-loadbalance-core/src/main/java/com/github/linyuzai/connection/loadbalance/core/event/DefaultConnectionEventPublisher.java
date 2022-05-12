package com.github.linyuzai.connection.loadbalance.core.event;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class DefaultConnectionEventPublisher extends AbstractConnectionEventPublisher {

    public DefaultConnectionEventPublisher() {
        this(new RethrowEventPublishErrorHandler());
    }

    public DefaultConnectionEventPublisher(EventPublishErrorHandler errorHandler) {
        super(new ArrayList<>(), errorHandler);
    }
}
