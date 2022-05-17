package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.utils.IdConnection;
import lombok.Getter;

@Getter
public class UnknownCloseEvent implements ConnectionEvent {

    private final Connection connection;

    private final Object reason;

    public UnknownCloseEvent(Object id, String type, Object reason, ConnectionLoadBalanceConcept concept) {
        this.connection = new IdConnection(id, type, concept);
        this.reason = reason;
    }
}
