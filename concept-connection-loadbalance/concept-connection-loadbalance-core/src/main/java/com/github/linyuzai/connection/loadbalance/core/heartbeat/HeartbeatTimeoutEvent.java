package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 心跳发送事件
 */
@Getter
@AllArgsConstructor
public class HeartbeatTimeoutEvent implements HeartbeatEvent {

    private Connection connection;
}
