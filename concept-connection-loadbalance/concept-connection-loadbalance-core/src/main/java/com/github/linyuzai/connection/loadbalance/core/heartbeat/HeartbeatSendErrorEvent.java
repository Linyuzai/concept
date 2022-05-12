package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class HeartbeatSendErrorEvent implements HeartbeatEvent, ErrorEvent {

    private Connection connection;

    private Throwable error;
}
