package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接建立事件。
 * <p>
 * Event will be published when connection established.
 */
@Getter
@RequiredArgsConstructor
public class ConnectionEstablishEvent extends TimestampEvent implements ConnectionEvent {

    private final Connection connection;
}
