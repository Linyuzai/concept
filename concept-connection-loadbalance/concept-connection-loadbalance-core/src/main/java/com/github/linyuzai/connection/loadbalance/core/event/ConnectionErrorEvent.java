package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

/**
 * 连接异常事件
 */
@Getter
public class ConnectionErrorEvent extends AbstractConnectionEvent implements ErrorEvent {

    private final Throwable error;

    public ConnectionErrorEvent(Connection connection, Throwable e) {
        super(connection);
        this.error = e;
    }
}
