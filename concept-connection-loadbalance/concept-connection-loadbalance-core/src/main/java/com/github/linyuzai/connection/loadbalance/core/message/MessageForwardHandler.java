package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

/**
 * 消息转发处理器
 * <p>
 * 当服务实例的客户端收到转发的消息后发送给自己真实的客户端连接
 */
public class MessageForwardHandler implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.SUBSCRIBER;
    }

    @Override
    public void onMessage(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
        try {
            concept.send(message);
            concept.getEventPublisher().publish(new MessageForwardEvent(connection, message));
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new MessageForwardErrorEvent(connection, message, e));
        }
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
