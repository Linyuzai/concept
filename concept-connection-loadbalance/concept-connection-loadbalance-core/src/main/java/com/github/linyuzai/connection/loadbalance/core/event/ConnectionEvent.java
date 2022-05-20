package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

/**
 * 连接事件
 */
public interface ConnectionEvent {

    Connection getConnection();
}
