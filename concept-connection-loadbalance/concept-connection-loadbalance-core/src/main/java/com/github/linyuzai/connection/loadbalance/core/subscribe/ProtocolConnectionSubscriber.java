package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public abstract class ProtocolConnectionSubscriber<T extends Connection> extends ServerInstanceConnectionSubscriber<T> {

    public abstract void setProtocol(String protocol);
}
