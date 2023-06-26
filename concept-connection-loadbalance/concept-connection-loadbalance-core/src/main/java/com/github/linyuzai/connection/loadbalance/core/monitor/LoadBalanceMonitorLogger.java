package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoadBalanceLogger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 连接负载均衡触发日志
 */
public class LoadBalanceMonitorLogger extends ConnectionLoadBalanceLogger implements ConnectionEventListener {

    public LoadBalanceMonitorLogger(Consumer<String> info, BiConsumer<String, Throwable> error) {
        super(info, error);
    }

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof LoadBalanceMonitorEvent) {
            info("Start running monitor for load balance");
        }
    }
}
