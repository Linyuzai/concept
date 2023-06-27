package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendErrorEvent;

/**
 * 心跳发送异常事件
 */
public class HeartbeatSendErrorEvent extends MessageSendErrorEvent implements HeartbeatEvent {

    public HeartbeatSendErrorEvent(Connection connection, Message message, Throwable error) {
        super(connection, message, error);
    }
}
