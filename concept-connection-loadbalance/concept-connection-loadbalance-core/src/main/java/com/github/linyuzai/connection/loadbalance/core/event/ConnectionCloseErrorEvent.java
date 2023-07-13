package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接关闭异常事件。
 * <p>
 * Event will be published when connection close error.
 */
@Getter
@RequiredArgsConstructor
public class ConnectionCloseErrorEvent extends TimestampEvent implements ConnectionEvent, ErrorEvent {

    private final Connection connection;

    private final Object reason;

    private final Throwable error;
}
