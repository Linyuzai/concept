package com.github.linyuzai.sync.waiting.core.exception;

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
