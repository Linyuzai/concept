package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;

/**
 * 消息转发处理器。
 * 当服务实例收到转发的消息后发送给自己的客户端连接。
 * <p>
 * Forward messages to clients which connected by this server instance
 * when received message from other service instance.
 */
public class MessageForwardHandler implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.SUBSCRIBER;
    }

    @Override
    public void onMessage(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
        try {
            String reused = message.getHeaders()
                    .getOrDefault(Message.REUSABLE, Boolean.FALSE.toString());
            if (Boolean.parseBoolean(reused)){
                concept.send(message.toReusableMessage());
            } else {
                concept.send(message);
            }
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
