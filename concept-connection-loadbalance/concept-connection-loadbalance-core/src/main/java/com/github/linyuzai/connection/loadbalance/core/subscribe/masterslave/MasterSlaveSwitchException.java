package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

/**
 * 主从切换异常。
 * <p>
 * Exception will be thrown when switch error.
 */
public class MasterSlaveSwitchException extends ConnectionLoadBalanceException {

    public MasterSlaveSwitchException(String message) {
        super(message);
    }

    public MasterSlaveSwitchException(String message, Throwable cause) {
        super(message, cause);
    }
}
