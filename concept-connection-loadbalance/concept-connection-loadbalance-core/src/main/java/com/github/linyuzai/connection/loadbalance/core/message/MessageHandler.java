package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

/**
 * 消息处理器
 * <p>
 * 用于接收客户端的消息
 */
public interface MessageHandler extends MessageReceiveEventListener {

    @Override
    default String getConnectionType() {
        return Connection.Type.CLIENT;
    }
}
