package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接建立事件
 */
@Getter
@RequiredArgsConstructor
public class ConnectionEstablishEvent implements ConnectionEvent {

    private final Connection connection;
}
