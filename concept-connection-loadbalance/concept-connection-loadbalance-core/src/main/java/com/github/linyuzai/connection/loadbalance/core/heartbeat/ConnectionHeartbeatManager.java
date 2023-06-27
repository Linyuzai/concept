package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 心跳管理器
 */
@Setter
@Getter
public class ConnectionHeartbeatManager extends ConnectionHeartbeatSupport {

    /**
     * 心跳间隔时间
     */
    private long period;

    /**
     * 初始化添加定时任务
     */
    @Override
    public void onInitialize(ConnectionLoadBalanceConcept concept) {
        concept.getScheduledExecutor().scheduleAtFixedRate(() -> schedule(concept),
                period, period, TimeUnit.MILLISECONDS);
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

    @Override
    public void onDestroy(ConnectionLoadBalanceConcept concept) {

    }
}
