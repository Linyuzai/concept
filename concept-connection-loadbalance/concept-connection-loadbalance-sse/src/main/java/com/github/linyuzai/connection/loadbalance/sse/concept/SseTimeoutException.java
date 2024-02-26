package com.github.linyuzai.connection.loadbalance.sse.concept;

public class SseTimeoutException extends RuntimeException {

    public SseTimeoutException(String message) {
        super(message);
    }

    public SseTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
