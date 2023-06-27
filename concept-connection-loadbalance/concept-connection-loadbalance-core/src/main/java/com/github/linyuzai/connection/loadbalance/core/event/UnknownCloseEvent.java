package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.UnknownConnection;
import lombok.Getter;

/**
 * 未知连接关闭事件
 */
@Getter
public class UnknownCloseEvent implements ConnectionEvent {

    private final Connection connection;

    private final Object reason;

    public UnknownCloseEvent(Object id, String type, Object reason, ConnectionLoadBalanceConcept concept) {
        this.connection = new UnknownConnection(id, type, concept);
        this.reason = reason;
    }
}
