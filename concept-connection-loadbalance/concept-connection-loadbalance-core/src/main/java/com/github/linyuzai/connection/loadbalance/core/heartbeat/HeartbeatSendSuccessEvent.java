package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendSuccessEvent;

/**
 * 心跳发送成功事件
 */
public class HeartbeatSendSuccessEvent extends MessageSendSuccessEvent implements HeartbeatEvent {

    public HeartbeatSendSuccessEvent(Connection connection, Message message) {
        super(connection, message);
    }
}
