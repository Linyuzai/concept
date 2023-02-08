package com.github.linyuzai.domain.core;

/**
 * 领域校验器
 */
public interface DomainValidator {

    /**
     * 校验
     */
    void validate(Object target);
}
