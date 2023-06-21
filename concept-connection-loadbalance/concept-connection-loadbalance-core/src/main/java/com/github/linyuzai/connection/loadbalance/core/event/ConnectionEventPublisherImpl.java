package com.github.linyuzai.connection.loadbalance.core.event;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认的事件发布者
 */
public class ConnectionEventPublisherImpl extends AbstractConnectionEventPublisher {

    public ConnectionEventPublisherImpl() {
        super(new CopyOnWriteArrayList<>());
    }
}
