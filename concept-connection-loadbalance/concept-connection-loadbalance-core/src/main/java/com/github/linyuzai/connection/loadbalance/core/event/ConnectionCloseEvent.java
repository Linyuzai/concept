package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接关闭事件
 */
@Getter
@RequiredArgsConstructor
public class ConnectionCloseEvent extends TimestampEvent implements ConnectionEvent {

    private final Connection connection;

    private final Object reason;
}
