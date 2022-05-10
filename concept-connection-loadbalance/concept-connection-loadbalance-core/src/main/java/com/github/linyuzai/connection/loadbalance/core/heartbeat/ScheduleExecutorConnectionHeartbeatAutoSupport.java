package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ScheduleExecutorConnectionHeartbeatAutoSupport extends ConnectionHeartbeatAutoSupport {

    private final ScheduledExecutorService executor;

    private final long period;

    public ScheduleExecutorConnectionHeartbeatAutoSupport(ConnectionLoadBalanceConcept concept,
                                                          String connectionType,
                                                          long timeout, long period,
                                                          ScheduledExecutorService executor) {
        super(concept, connectionType, timeout);
        this.executor = executor;
        this.period = period;
    }

    @Override
    public void onInitialize() {
        executor.scheduleAtFixedRate(this::schedule, period, period, TimeUnit.MILLISECONDS);
    }

    public abstract void schedule();

    @Override
    public void onDestroy() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
