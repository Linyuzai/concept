package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

/**
 * 连接负载均衡触发日志
 */
public class LoadBalanceMonitorLogger extends AbstractScoped implements ConnectionEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof LoadBalanceMonitorEvent) {
            concept.getLogger().info("Start running monitor for load balance");
        }
    }
}
