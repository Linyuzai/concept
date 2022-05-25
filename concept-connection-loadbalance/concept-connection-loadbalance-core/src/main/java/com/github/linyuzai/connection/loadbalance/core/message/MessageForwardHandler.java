package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

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
    public void onMessage(Message message, Connection connection) {
        try {
            connection.getConcept().send(message);
            connection.getConcept().publish(new MessageForwardEvent(connection, message));
        } catch (Throwable e) {
            connection.getConcept().publish(new MessageForwardErrorEvent(connection, message, e));
        }
    }
}
