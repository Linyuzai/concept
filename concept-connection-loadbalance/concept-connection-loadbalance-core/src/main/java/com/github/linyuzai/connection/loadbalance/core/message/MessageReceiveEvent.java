package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import lombok.Getter;

/**
 * 消息接收事件
 */
@Getter
public class MessageReceiveEvent extends AbstractConnectionEvent implements MessageEvent {

    private final Message message;

    public MessageReceiveEvent(Connection connection, Message message) {
        super(connection);
        this.message = message;
    }
}
