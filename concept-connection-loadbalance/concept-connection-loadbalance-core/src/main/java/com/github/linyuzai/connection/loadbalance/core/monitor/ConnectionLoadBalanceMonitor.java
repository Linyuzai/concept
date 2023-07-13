package com.github.linyuzai.connection.loadbalance.core.monitor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

/**
 * 连接负载均衡监控器
 * <p>
 * 当服务实例之间的转发连接断开时会尝试重新连接
 */
public interface ConnectionLoadBalanceMonitor {

    void start(ConnectionLoadBalanceConcept concept);

    void stop(ConnectionLoadBalanceConcept concept);
}
