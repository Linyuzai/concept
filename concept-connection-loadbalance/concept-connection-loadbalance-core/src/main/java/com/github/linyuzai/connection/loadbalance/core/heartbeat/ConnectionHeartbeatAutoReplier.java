package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.concurrent.ScheduledExecutorService;

public class ConnectionHeartbeatAutoReplier extends ScheduleExecutorConnectionHeartbeatAutoSupport {

    public ConnectionHeartbeatAutoReplier(ConnectionLoadBalanceConcept concept,
                                          String connectionType,
                                          long timeout, long period,
                                          ScheduledExecutorService executor) {
        super(concept, connectionType, timeout, period, executor);
    }

    @Override
    public void schedule() {
        closeTimeout();
    }
}
