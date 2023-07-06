package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

public class MasterSlaveSwitchException extends ConnectionLoadBalanceException {

    public MasterSlaveSwitchException(String message) {
        super(message);
    }

    public MasterSlaveSwitchException(String message, Throwable cause) {
        super(message, cause);
    }
}
