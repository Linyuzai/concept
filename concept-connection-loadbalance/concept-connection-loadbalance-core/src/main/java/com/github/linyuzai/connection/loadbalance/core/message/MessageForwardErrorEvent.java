package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import lombok.Getter;

/**
 * 消息转发异常事件
 */
@Getter
public class MessageForwardErrorEvent extends AbstractConnectionEvent implements ErrorEvent {

    private final Message message;

    private final Throwable error;

    public MessageForwardErrorEvent(Connection connection, Message message, Throwable error) {
        super(connection);
        this.message = message;
        this.error = error;
    }
}
