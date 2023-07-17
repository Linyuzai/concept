package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageReceiveEventListener;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

/**
 * 连接订阅处理器。
 * 当接收到服务实例信息后，对该服务实例反向连接。
 * <p>
 * Connection subscribe handler.
 * When receive server instance info, reverse connect to the server instance.
 */
public class ConnectionSubscribeHandler extends AbstractScoped implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.OBSERVABLE;
    }

    @Override
    public void onMessage(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
        ConnectionSubscriber subscriber = unwrap(concept.getConnectionSubscriber());
        if (message.getPayload() instanceof ConnectionServer &&
                subscriber instanceof ServerInstanceConnectionSubscriber) {
            ConnectionServer server = message.getPayload();
            connection.getMetadata().put(ConnectionServer.class, server);
            ((ServerInstanceConnectionSubscriber<?>) subscriber).subscribe(server, concept);
        }
    }

    protected ConnectionSubscriber unwrap(ConnectionSubscriber subscriber) {
        if (subscriber instanceof ConnectionSubscriber.Delegate) {
            return unwrap(((ConnectionSubscriber.Delegate) subscriber).getDelegate());
        } else {
            return subscriber;
        }
    }
}
