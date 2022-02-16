package com.github.linyuzai.sync.waiting.core.exception;

/**
 * 同步等待超时异常。
 */
public class SyncWaitingTimeoutException extends SyncWaitingException {

    public SyncWaitingTimeoutException(String message) {
        super(message);
    }

    public SyncWaitingTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyncWaitingTimeoutException(Throwable cause) {
        super(cause);
    }
}
