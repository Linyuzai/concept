package com.github.linyuzai.domain.core.exception;

public class DomainMultipleRecordsException extends DomainException {

    public DomainMultipleRecordsException(int count) {
        super(count + " records found");
    }
}
