package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.NoArgsConstructor;

/**
 * 订阅消息
 * <p>
 * 用于发送服务信息和反向连接
 */
@NoArgsConstructor
public class SubscribeMessage extends AbstractMessage<ConnectionServer> {

    public SubscribeMessage(ConnectionServer payload) {
        setPayload(payload);
    }
}
