package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

/**
 * 连接关闭事件
 */
@Getter
public class ConnectionCloseErrorEvent extends AbstractConnectionEvent implements ErrorEvent {

    private final Object reason;

    private final Throwable error;

    public ConnectionCloseErrorEvent(Connection connection, Object reason, Throwable e) {
        super(connection);
        this.reason = reason;
        this.error = e;
    }
}
