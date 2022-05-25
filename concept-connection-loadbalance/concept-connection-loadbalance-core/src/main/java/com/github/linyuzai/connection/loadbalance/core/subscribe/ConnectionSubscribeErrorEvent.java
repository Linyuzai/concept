package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 连接订阅异常事件
 */
@Getter
@AllArgsConstructor
public class ConnectionSubscribeErrorEvent implements ErrorEvent {

    private final ConnectionServer connectionServer;

    private final Throwable error;
}
