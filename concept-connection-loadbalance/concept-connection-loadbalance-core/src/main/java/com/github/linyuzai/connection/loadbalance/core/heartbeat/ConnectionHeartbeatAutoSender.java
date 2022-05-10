package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.concurrent.ScheduledExecutorService;

public class ConnectionHeartbeatAutoSender extends ScheduleExecutorConnectionHeartbeatAutoSupport {

    public ConnectionHeartbeatAutoSender(ConnectionLoadBalanceConcept concept,
                                         String connectionType,
                                         long timeout, long period,
                                         ScheduledExecutorService executor) {
        super(concept, connectionType, timeout, period, executor);
    }

    @Override
    public void schedule() {
        closeTimeout();
        sendPing();
    }
}
