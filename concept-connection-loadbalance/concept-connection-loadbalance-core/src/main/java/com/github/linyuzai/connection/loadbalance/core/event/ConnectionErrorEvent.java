package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接异常事件。
 * <p>
 * Event will be published when connection has error.
 */
@Getter
@RequiredArgsConstructor
public class ConnectionErrorEvent extends TimestampEvent implements ConnectionEvent, ErrorEvent {

    private final Connection connection;

    private final Throwable error;
}
