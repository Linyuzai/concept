package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;

/**
 * 消息解码器
 */
public interface MessageDecoder {

    Message decode(Object message);
}
