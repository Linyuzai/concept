package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;

import java.util.Collection;

/**
 * 消息准备事件
 * <p>
 * 在消息还未发送时发布
 */
@Getter
public class MessagePrepareEvent extends TimestampEvent implements MessageEvent {

    private final Message message;

    private final Collection<Connection> connections;

    public MessagePrepareEvent(Message message, Collection<Connection> connections) {
        this.message = message;
        this.connections = connections;
    }
}
