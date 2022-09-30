package com.github.linyuzai.extension.core.exception;

public class ExtensionExistException extends ExtensionException {

    public ExtensionExistException(String id) {
        super("Extension existed with id: " + id);
    }
}
