package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MasterSlaveSwitchEvent extends TimestampEvent {

    private final MasterSlaveConnection connection;

    private final MasterSlave switchTo;
}
