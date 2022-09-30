package com.github.linyuzai.extension.core.exception;

public class ExtensionException extends RuntimeException {

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
