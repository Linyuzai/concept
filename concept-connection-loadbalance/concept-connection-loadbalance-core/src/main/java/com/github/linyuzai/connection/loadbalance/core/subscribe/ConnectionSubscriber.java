package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

import java.util.function.Consumer;

/**
 * 连接订阅者
 * <p>
 * 可以理解为服务实例对其他服务的消息进行监听
 */
public interface ConnectionSubscriber {
    void subscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept, Consumer<? extends Connection> consumer);
}
