package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 通过定时任务周期性的触发监控
 */
@Getter
@Setter
public class ScheduledConnectionLoadBalanceMonitor extends AbstractScoped
        implements ConnectionLoadBalanceMonitor, ConnectionEventListener {

    private long period;

    public void start(ConnectionLoadBalanceConcept concept) {
        concept.getScheduledExecutor().scheduleAtFixedRate(() -> subscribe(concept),
                period, period, TimeUnit.MILLISECONDS);
    }

    public void subscribe(ConnectionLoadBalanceConcept concept) {
        concept.getEventPublisher().publish(new LoadBalanceMonitorEvent());
        concept.getConnectionSubscriber().subscribe(concept::onEstablish);
    }

    public void stop(ConnectionLoadBalanceConcept concept) {

    }

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            start(concept);
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            stop(concept);
        }
    }
}
