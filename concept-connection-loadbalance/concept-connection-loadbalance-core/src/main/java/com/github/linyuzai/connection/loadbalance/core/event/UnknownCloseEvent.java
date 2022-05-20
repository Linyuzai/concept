package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.extension.IdConnection;
import lombok.Getter;

@Getter
public class UnknownCloseEvent implements ConnectionEvent {

    private final Connection connection;

    private final Object reason;

    public UnknownCloseEvent(Object id, String type, Object reason, ConnectionLoadBalanceConcept concept) {
        this(new IdConnection(id, type, concept), reason);
    }

    public UnknownCloseEvent(Connection connection, Object reason) {
        this.connection = connection;
        this.reason = reason;
    }
}
