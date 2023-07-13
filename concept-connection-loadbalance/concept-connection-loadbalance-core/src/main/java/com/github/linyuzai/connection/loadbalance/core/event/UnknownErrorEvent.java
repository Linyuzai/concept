package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.UnknownConnection;
import lombok.Getter;

/**
 * 未知连接异常事件。
 * <p>
 * Event will be published when unknown connection has error.
 */
@Getter
public class UnknownErrorEvent extends TimestampEvent implements ConnectionEvent, ErrorEvent {

    private final Connection connection;

    private final Throwable error;

    public UnknownErrorEvent(Object id, String type, Throwable e, ConnectionLoadBalanceConcept concept) {
        this.connection = new UnknownConnection(id, type, concept);
        this.error = e;
    }
}
