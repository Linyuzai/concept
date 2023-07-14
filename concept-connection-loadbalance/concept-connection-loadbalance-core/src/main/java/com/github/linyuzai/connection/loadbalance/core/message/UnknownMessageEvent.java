package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.UnknownConnection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;

/**
 * 未知消息事件。
 * <p>
 * Event will be published when message received from unknown connection.
 */
@Getter
public class UnknownMessageEvent extends TimestampEvent implements MessageEvent {

    private final Connection connection;

    private final Message message;

    public UnknownMessageEvent(Object id, String type, Object message, ConnectionLoadBalanceConcept concept) {
        this.connection = new UnknownConnection(id, type, concept);
        this.message = new ObjectMessage(message);
    }
}
