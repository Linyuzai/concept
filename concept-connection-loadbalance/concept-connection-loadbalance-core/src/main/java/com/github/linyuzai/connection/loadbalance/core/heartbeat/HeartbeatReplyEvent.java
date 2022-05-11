package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeartbeatReplyEvent implements HeartbeatEvent {

    private Connection connection;
}
