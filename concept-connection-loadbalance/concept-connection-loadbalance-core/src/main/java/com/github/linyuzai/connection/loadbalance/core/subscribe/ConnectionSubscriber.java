package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

/**
 * 连接订阅者
 * <p>
 * 可以理解为服务实例对其他服务的消息进行监听
 * <p>
 * 或是监听 Redis 和 MQ
 */
public interface ConnectionSubscriber {

    void subscribe(ConnectionLoadBalanceConcept concept);
}
