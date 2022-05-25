package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过定时任务周期性的触发监控
 */
@RequiredArgsConstructor
public class ScheduledConnectionLoadBalanceMonitor implements ConnectionLoadBalanceMonitor, ConnectionEventListener {

    private ConnectionLoadBalanceConcept concept;

    @NonNull
    private final ScheduledExecutorService executor;

    private final long period;

    public void start() {
        executor.scheduleAtFixedRate(this::subscribe, period, period, TimeUnit.MILLISECONDS);
    }

    public void subscribe() {
        concept.publish(new LoadBalanceMonitorEvent());
        concept.subscribe(true);
    }

    public void stop() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            concept = ((ConnectionLoadBalanceConceptInitializeEvent) event).getConcept();
            start();
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            stop();
        }
    }
}
