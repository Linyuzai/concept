package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.NoArgsConstructor;

/**
 * 订阅消息。
 * 用于发送服务信息和反向连接。
 * <p>
 * Message of subscribe.
 * Used to send server info and reverse connection.
 */
@NoArgsConstructor
public class SubscribeMessage extends AbstractMessage<ConnectionServer> {

    public SubscribeMessage(ConnectionServer payload) {
        setPayload(payload);
    }
}
