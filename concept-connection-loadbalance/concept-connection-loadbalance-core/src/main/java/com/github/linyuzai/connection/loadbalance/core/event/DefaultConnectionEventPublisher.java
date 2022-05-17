package com.github.linyuzai.connection.loadbalance.core.event;

import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultConnectionEventPublisher extends AbstractConnectionEventPublisher {

    public DefaultConnectionEventPublisher() {
        super(new CopyOnWriteArrayList<>());
    }
}
