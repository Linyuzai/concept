package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 心跳管理器。
 * <p>
 * Management of heartbeat.
 */
@Setter
@Getter
public class ConnectionHeartbeatManager extends ConnectionHeartbeatSupport {

    /**
     * 心跳发送间隔时间。
     * <p>
     * Period of heartbeat sending.
     */
    private long period;

    /**
     * 初始化添加定时任务。
     * <p>
     * Add heartbeat jab.
     */
    @Override
    public void onInitialize(ConnectionLoadBalanceConcept concept) {
        concept.getScheduledExecutor().scheduleAtFixedRate(() -> schedule(concept),
                period, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭心跳超时的连接并发送心跳。
     * <p>
     * Close connections on heartbeat timeout and send heartbeat.
     */
    public void schedule(ConnectionLoadBalanceConcept concept) {
        closeTimeout(concept);
        sendHeartbeat(concept);
    }

    @Override
    public void onDestroy(ConnectionLoadBalanceConcept concept) {

    }
}
