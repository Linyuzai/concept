package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;

/**
 * 消息接收事件监听器
 */
public interface MessageReceiveEventListener extends ConnectionEventListener {

    @Override
    default void onEvent(Object event) {
        if (event instanceof MessageReceiveEvent) {
            Message message = ((MessageReceiveEvent) event).getMessage();
            if (message instanceof PingMessage || message instanceof PongMessage) {
                return;
            }
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            if (connection.getType().equals(getConnectionType())) {
                onMessage(message, connection);
            }
        }
    }

    String getConnectionType();

    void onMessage(Message message, Connection connection);
}
