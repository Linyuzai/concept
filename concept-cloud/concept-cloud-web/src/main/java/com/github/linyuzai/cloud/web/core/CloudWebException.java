package com.github.linyuzai.cloud.web.core;

public class CloudWebException extends RuntimeException {

    public CloudWebException(String message) {
        super(message);
    }

    public CloudWebException(String message, Throwable cause) {
        super(message, cause);
    }
}
