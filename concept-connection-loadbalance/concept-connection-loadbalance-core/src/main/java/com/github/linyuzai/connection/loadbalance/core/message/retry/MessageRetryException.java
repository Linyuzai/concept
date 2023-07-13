package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import lombok.Getter;

/**
 * 消息重试异常。
 * <p>
 * Exception for message retry error.
 */
@Getter
public class MessageRetryException extends ConnectionLoadBalanceException {

    /**
     * 重试原因。
     * 如发送失败的异常或是上一次重试失败的异常。
     * <p>
     * Retry for this error.
     */
    private final Throwable retryFor;

    public MessageRetryException(String message, Throwable retryFor) {
        super(message);
        this.retryFor = retryFor;
    }

    public MessageRetryException(String message, Throwable cause, Throwable retryFor) {
        super(message, cause);
        this.retryFor = retryFor;
    }
}
