package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 主从切换事件。
 * <p>
 * Event will be published when switch.
 */
@Getter
@RequiredArgsConstructor
public class MasterSlaveSwitchEvent extends TimestampEvent {

    private final MasterSlaveConnection connection;

    private final MasterSlave switchTo;
}
