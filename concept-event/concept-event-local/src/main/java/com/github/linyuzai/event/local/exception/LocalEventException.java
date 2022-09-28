package com.github.linyuzai.event.local.exception;

import com.github.linyuzai.event.core.exception.EventException;

/**
 * 本地事件异常
 */
public class LocalEventException extends EventException {

    public LocalEventException(String message) {
        super(message);
    }

    public LocalEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
