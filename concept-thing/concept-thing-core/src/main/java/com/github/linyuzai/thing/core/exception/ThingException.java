package com.github.linyuzai.thing.core.exception;

public class ThingException extends RuntimeException {

    public ThingException(String message) {
        super(message);
    }

    public ThingException(String message, Throwable cause) {
        super(message, cause);
    }
}
