package com.github.linyuzai.event.rabbitmq.exception;

import com.github.linyuzai.event.core.exception.EventException;

/**
 * RabbitMQ 事件异常
 */
public class RabbitEventException extends EventException {

    public RabbitEventException(String message) {
        super(message);
    }

    public RabbitEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
