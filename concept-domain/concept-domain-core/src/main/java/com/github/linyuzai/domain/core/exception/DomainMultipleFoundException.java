package com.github.linyuzai.domain.core.exception;

public class DomainMultipleFoundException extends DomainException {

    public DomainMultipleFoundException(Class<?> type) {
        super(type.getSimpleName() + " multiple found");
    }

    public DomainMultipleFoundException(Class<?> type, long count) {
        super(type.getSimpleName() + " multiple found: " + count);
    }
}
