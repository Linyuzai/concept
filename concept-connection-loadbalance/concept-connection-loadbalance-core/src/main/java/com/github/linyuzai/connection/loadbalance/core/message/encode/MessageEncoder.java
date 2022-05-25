package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;

/**
 * 消息编码器
 */
public interface MessageEncoder {

    Object encode(Message message);
}
