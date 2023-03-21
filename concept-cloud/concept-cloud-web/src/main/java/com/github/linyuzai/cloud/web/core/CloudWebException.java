package com.github.linyuzai.cloud.web.core;

public class CloudWebException extends RuntimeException {

    private static final long serialVersionUID = 3365246484837672837L;

    public CloudWebException(String message) {
        super(message);
    }

    public CloudWebException(String message, Throwable cause) {
        super(message, cause);
    }
}
