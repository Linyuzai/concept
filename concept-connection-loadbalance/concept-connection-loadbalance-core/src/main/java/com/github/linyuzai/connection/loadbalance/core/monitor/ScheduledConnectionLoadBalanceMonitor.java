package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过定时任务周期性的触发监控
 */
@Getter
@RequiredArgsConstructor
public class ScheduledConnectionLoadBalanceMonitor extends AbstractScoped
        implements ConnectionLoadBalanceMonitor, ConnectionEventListener {

    @NonNull
    private final ScheduledExecutorService executor;

    private final long period;

    private ConnectionLoadBalanceConcept concept;

    public void start() {
        executor.scheduleAtFixedRate(this::subscribe, period, period, TimeUnit.MILLISECONDS);
    }

    public void subscribe() {
        concept.getEventPublisher().publish(new LoadBalanceMonitorEvent());
        concept.getConnectionSubscriber().subscribe(concept);
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
