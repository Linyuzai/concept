package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

public class MessageDecodeException extends ConnectionLoadBalanceException {

    public MessageDecodeException(Object message) {
        super("Message can not decode " + message);
    }
}
