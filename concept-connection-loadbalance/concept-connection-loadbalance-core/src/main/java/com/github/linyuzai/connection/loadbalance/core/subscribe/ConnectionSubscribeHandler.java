package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageReceiveEventListener;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

/**
 * 连接订阅处理器
 * <p>
 * 当接收到服务实例信息后
 * <p>
 * 对该服务实例反向连接
 */
public class ConnectionSubscribeHandler implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.OBSERVABLE;
    }

    @Override
    public void onMessage(Message message, Connection connection) {
        ConnectionSubscriber subscriber = connection.getConcept().getConnectionSubscriber();
        if (message.isType(ConnectionServer.class) && subscriber instanceof ServerConnectionSubscriber) {
            ConnectionServer server = message.getPayload();
            connection.getMetadata().put(ConnectionServer.class, server);
            ((ServerConnectionSubscriber<?>) subscriber).subscribe(server, connection.getConcept());
        }
    }
}
