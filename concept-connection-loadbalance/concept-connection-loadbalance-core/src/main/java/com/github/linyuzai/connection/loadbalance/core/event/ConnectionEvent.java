package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

/**
 * 连接事件。
 * <p>
 * Connection's event.
 */
public interface ConnectionEvent {

    Connection getConnection();
}
