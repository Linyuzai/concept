package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 主从切换异常事件。
 * <p>
 * Event will be published when switch error.
 */
@Getter
@RequiredArgsConstructor
public class MasterSlaveSwitchErrorEvent extends TimestampEvent implements ErrorEvent {

    private final Throwable error;

    private final MasterSlave switchTo;
}
