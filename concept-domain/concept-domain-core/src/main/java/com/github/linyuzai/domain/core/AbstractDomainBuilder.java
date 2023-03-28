package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.exception.DomainException;

/**
 * 领域模型 Builder 抽象类
 * <p>
 * 自带 {@link DomainValidator} 校验
 *
 * @param <T>
 */
public abstract class AbstractDomainBuilder<T extends DomainObject> implements DomainBuilder<T> {

    @Override
    public T build(DomainValidator validator) {
        init();
        validate(validator);
        return build();
    }

    /**
     * 校验
     */
    protected void validate(DomainValidator validator) {
        if (validator == null) {
            throw new DomainException("DomainValidator is null");
        }
        validator.validate(this);
    }

    protected void init() {

    }

    /**
     * 需要子类实现的 build 流程
     */
    protected abstract T build();
}
