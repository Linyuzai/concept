package com.github.linyuzai.domain.core;

import java.util.Objects;

/**
 * 领域模型 Builder 抽象类
 * <p>
 * 自带 {@link DomainValidator} 校验
 *
 * @param <T>
 * @param <B>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDomainBuilder<T extends DomainObject, B> implements DomainBuilder<T> {

    protected DomainValidator validator;

    public B validator(DomainValidator validator) {
        this.validator = validator;
        return (B) this;
    }

    @Override
    public T build() {
        initDefaultValue();
        validate();
        return doBuild();
    }

    /**
     * 校验
     */
    protected void validate() {
        Objects.requireNonNull(validator).validate(this);
    }

    /**
     * 初始化默认值
     */
    protected void initDefaultValue() {

    }

    /**
     * 需要子类实现的 build 流程
     */
    protected abstract T doBuild();
}
