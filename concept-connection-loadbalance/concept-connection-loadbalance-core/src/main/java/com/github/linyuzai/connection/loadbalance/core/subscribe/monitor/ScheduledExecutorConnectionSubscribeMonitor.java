package com.github.linyuzai.connection.loadbalance.core.subscribe.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ScheduledExecutorConnectionSubscribeMonitor implements ConnectionSubscribeMonitor, ConnectionEventListener {

    private ConnectionLoadBalanceConcept concept;

    @NonNull
    private final ScheduledExecutorService executor;

    private final long period;

    public void start() {
        executor.scheduleAtFixedRate(this::subscribe, period, period, TimeUnit.MILLISECONDS);
    }

    public void subscribe() {
        try {
            concept.subscribe(false);
            concept.publish(new SubscribeMonitorEvent());
        } catch (Throwable e) {
            //TODO
        }
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
