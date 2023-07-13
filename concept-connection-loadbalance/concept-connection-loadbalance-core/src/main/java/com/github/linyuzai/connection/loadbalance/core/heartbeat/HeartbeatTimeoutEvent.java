package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 心跳超时事件。
 * <p>
 * Event will be published when heartbeat timeout.
 */
@Getter
@RequiredArgsConstructor
public class HeartbeatTimeoutEvent extends TimestampEvent {

    private final Connection connection;
}
