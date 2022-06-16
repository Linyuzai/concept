package com.github.linyuzai.router.core.exception;

public class RouterException extends RuntimeException {

    public RouterException(String message) {
        super(message);
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }
}
