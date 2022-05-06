package com.github.linyuzai.connection.loadbalance.core.subscribe.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ScheduledExecutorConnectionSubscribeMonitor implements ConnectionSubscribeMonitor {

    private final ConnectionLoadBalanceConcept concept;

    private final ScheduledExecutorService executor;

    private final long period;

    public ScheduledExecutorConnectionSubscribeMonitor(ConnectionLoadBalanceConcept concept, long period) {
        this(concept, Executors.newSingleThreadScheduledExecutor(), period);
    }

    private volatile ScheduledFuture<?> future;

    @Override
    public void start() {
        if (future == null) {
            future = executor.scheduleAtFixedRate(this::subscribe, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    public void subscribe() {
        concept.subscribe(false, false);
    }

    @Override
    public void stop() {
        if (future != null && !future.isCancelled()) {
            future.cancel(true);
            future = null;
        }
    }
}
