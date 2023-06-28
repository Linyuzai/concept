package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

/**
 * 消息编码异常
 */
public class MessageEncodeException extends ConnectionLoadBalanceException {

    public MessageEncodeException(Message message, Throwable cause) {
        super("Can not encode message: " + message, cause);
    }
}
