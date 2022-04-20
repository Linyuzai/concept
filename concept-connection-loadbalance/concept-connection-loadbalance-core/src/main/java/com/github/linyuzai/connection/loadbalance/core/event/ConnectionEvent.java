package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public interface ConnectionEvent {

    Connection getConnection();
}
