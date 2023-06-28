package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.concept.UnknownConnection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;

/**
 * 未知消息
 * <p>
 * 当对应的连接不存在连接仓库中时发布
 */
@Getter
public class UnknownMessageEvent extends TimestampEvent implements ConnectionEvent, MessageEvent {

    private final Connection connection;

    private final Message message;

    public UnknownMessageEvent(Object id, String type, Object message, ConnectionLoadBalanceConcept concept) {
        this.connection = new UnknownConnection(id, type, concept);
        this.message = new ObjectMessage(message);
    }
}
