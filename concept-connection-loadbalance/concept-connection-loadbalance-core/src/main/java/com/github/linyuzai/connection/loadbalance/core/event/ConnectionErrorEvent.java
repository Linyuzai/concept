package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接异常事件
 */
@Getter
@RequiredArgsConstructor
public class ConnectionErrorEvent implements ConnectionEvent, ErrorEvent {

    private final Connection connection;

    private final Throwable error;
}
