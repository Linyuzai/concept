package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import lombok.Getter;

/**
 * 消息发送异常事件
 */
@Getter
public class MessageSendErrorEvent extends AbstractConnectionEvent implements MessageEvent, ErrorEvent {

    private final Message message;

    private final Throwable error;

    public MessageSendErrorEvent(Connection connection, Message message, Throwable error) {
        super(connection);
        this.message = message;
        this.error = error;
    }
}
