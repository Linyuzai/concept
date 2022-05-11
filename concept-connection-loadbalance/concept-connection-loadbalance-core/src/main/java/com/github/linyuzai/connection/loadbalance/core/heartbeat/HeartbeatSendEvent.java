package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class HeartbeatSendEvent implements HeartbeatEvent {

    private Collection<Connection> connections;

    private String connectionType;
}
