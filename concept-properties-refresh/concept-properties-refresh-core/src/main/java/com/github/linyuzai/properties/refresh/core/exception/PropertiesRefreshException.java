package com.github.linyuzai.properties.refresh.core.exception;

public class PropertiesRefreshException extends RuntimeException {

    public PropertiesRefreshException(String message) {
        super(message);
    }

    public PropertiesRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertiesRefreshException(Throwable cause) {
        super(cause);
    }
}
