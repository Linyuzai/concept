package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.TimestampEvent;
import lombok.Getter;

import java.util.Collection;

/**
 * 消息发送事件
 * <p>
 * 在消息发送之后发布
 * <p>
 * 无论成功或失败都会发布
 */
@Getter
public class MessageSendEvent extends TimestampEvent implements MessageEvent {

    private final Message message;

    private final Collection<Connection> connections;

    public MessageSendEvent(Message message, Collection<Connection> connections) {
        this.message = message;
        this.connections = connections;
    }
}
