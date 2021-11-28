package com.github.linyuzai.sync.waiting.core.exception;

public class SyncWaitingException extends RuntimeException {

    public SyncWaitingException(String message) {
        super(message);
    }

    public SyncWaitingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyncWaitingException(Throwable cause) {
        super(cause);
    }
}
