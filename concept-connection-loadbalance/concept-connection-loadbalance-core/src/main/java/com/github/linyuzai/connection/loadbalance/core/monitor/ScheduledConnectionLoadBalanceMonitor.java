package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeErrorEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 通过定时任务周期性的触发监控。
 * <p>
 * The monitor to resubscribe by scheduled task when disconnect.
 */
@Getter
@Setter
public class ScheduledConnectionLoadBalanceMonitor extends AbstractScoped
        implements ConnectionLoadBalanceMonitor, ConnectionEventListener {

    /**
     * 监控周期。
     * <p>
     * The period of monitor.
     */
    private long period;

    /**
     * 是否启用日志。
     * <p>
     * Whether enable logger.
     */
    private boolean loggerEnabled;

    /**
     * 是否已停止。
     * <p>
     * Whether it has been stopped.
     */
    private volatile boolean stopped;

    @Override
    public void start(ConnectionLoadBalanceConcept concept) {
        concept.getScheduledExecutor().scheduleAtFixedRate(() -> subscribe(concept),
                period, period, TimeUnit.MILLISECONDS);
    }

    public void subscribe(ConnectionLoadBalanceConcept concept) {
        if (stopped) {
            return;
        }
        if (loggerEnabled) {
            concept.getLogger().info("Start running monitor for load balance");
        }
        concept.getEventPublisher().publish(new LoadBalanceMonitorEvent());
        concept.getConnectionSubscriber().subscribe();
    }

    @Override
    public void stop(ConnectionLoadBalanceConcept concept) {
        stopped = true;
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
