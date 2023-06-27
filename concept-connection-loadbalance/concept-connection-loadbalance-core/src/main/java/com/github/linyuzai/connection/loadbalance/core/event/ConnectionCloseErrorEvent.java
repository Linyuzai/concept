package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接关闭事件
 */
@Getter
@RequiredArgsConstructor
public class ConnectionCloseErrorEvent implements ConnectionEvent, ErrorEvent {

    private final Connection connection;

    private final Object reason;

    private final Throwable error;
}
