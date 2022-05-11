package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ScheduledExecutorConnectionHeartbeatAutoSupport extends ConnectionHeartbeatAutoSupport {

    private final ScheduledExecutorService executor;

    private final long period;

    public ScheduledExecutorConnectionHeartbeatAutoSupport(String connectionType,
                                                           long timeout, long period,
                                                           ScheduledExecutorService executor) {
        super(connectionType, timeout);
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
