package com.github.linyuzai.domain.core.exception;

public class DomainNotFoundException extends DomainException {

    public DomainNotFoundException(Class<?> type, String id) {
        super(type.getSimpleName() + " not found: " + id);
    }
}
