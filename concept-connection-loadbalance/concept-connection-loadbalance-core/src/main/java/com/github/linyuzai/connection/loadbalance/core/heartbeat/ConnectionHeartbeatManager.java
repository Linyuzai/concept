package com.github.linyuzai.connection.loadbalance.core.heartbeat;

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
    public void onInitialize() {
        executor.scheduleAtFixedRate(this::schedule, period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭心跳超时的连接
     * <p>
     * 发送 ping
     */
    public void schedule() {
        closeTimeout();
        sendPing();
    }

    /**
     * 关闭线程池调度器
     */
    @Override
    public void onDestroy() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
