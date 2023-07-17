package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接订阅异常事件。
 * <p>
 * Event will be published when subscribe error.
 */
@Getter
@RequiredArgsConstructor
public class ConnectionSubscribeErrorEvent extends TimestampEvent implements ErrorEvent {

    private final Throwable error;
}
