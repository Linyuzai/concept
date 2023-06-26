package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 心跳管理器
 */
public class ConnectionHeartbeatManager extends ConnectionHeartbeatSupport {

    private final ScheduledExecutorService executor;

    /**
     * 心跳间隔时间
     */
    private final long period;

    public ConnectionHeartbeatManager(String connectionType,
                                      long timeout, long period,
                                      ScheduledExecutorService executor) {
        this(Collections.singletonList(connectionType), timeout, period, executor);
    }

    public ConnectionHeartbeatManager(Collection<String> connectionTypes,
                                      long timeout, long period,
                                      ScheduledExecutorService executor) {
        super(connectionTypes, timeout);
        this.executor = executor;
        this.period = period;
    }

    /**
     * 初始化添加定时任务
     */
    @Override
    public void onInitialize(ConnectionLoadBalanceConcept concept) {
        executor.scheduleAtFixedRate(() -> schedule(concept), period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭心跳超时的连接
     * <p>
     * 发送 ping
     */
    public void schedule(ConnectionLoadBalanceConcept concept) {
        closeTimeout(concept);
        sendPing(concept);
    }

    /**
     * 关闭线程池调度器
     */
    @Override
    public void onDestroy(ConnectionLoadBalanceConcept concept) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
