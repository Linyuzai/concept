package com.github.linyuzai.connection.loadbalance.core.executor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务执行器实现。
 * 基于 {@link ScheduledExecutorService}。
 * <p>
 * Scheduled executor impl by {@link ScheduledExecutorService}.
 */
@Getter
@RequiredArgsConstructor
public class ScheduledExecutorImpl implements ScheduledExecutor {

    private final ScheduledExecutorService service;

    @Override
    public void schedule(Runnable runnable, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept) {
        service.schedule(runnable, delay, unit);
    }

    @Override
    public void scheduleAtFixedRate(Runnable runnable, long initialDelay, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept) {
        service.scheduleAtFixedRate(runnable, initialDelay, delay, unit);
    }

    @Override
    public void scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept) {
        service.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
        service.shutdown();
    }

    @Override
    public void shutdown(ConnectionLoadBalanceConcept concept) {
        if (service.isShutdown()) {
            return;
        }
        service.shutdown();
    }
}
