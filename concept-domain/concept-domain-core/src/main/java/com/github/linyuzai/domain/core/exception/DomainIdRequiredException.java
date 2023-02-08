package com.github.linyuzai.domain.core.exception;

public class DomainIdRequiredException extends DomainRequiredException {

    public DomainIdRequiredException(Class<?> type) {
        super(type.getSimpleName() + " id");
    }
}
