package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ErrorEvent;
import lombok.Getter;

/**
 * 消息解码异常事件
 */
@Getter
public class MessageDecodeErrorEvent extends AbstractConnectionEvent implements ErrorEvent {

    private final Throwable error;

    public MessageDecodeErrorEvent(Connection connection, Throwable error) {
        super(connection);
        this.error = error;
    }
}
